package org.inu.events.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.inu.events.MainActivity
import org.inu.events.R
import org.inu.events.data.model.entity.Fcm
import org.inu.events.data.repository.FcmRepository
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService:  FirebaseMessagingService(){
    private val fcmRepository: FcmRepository by inject()

    override fun onNewToken(p0: String) {
        Log.d("FCM TOKEN : ", p0)

        // todo 로그인 유무 확인후 token 값 서버로 전달
        sendFcmToken(p0)
        super.onNewToken(p0)
    }

    private fun sendFcmToken(fcmToken:String){
        fcmRepository.postFcmToken(
            fcmToken
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]


        NotificationManagerCompat.from(this)
            .notify((System.currentTimeMillis()/100).toInt(),createNotification(title, message))
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this,(System.currentTimeMillis()/100).toInt(),intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // todo 알람 디자인 나오면 수정
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }
    }

    companion object {
        private const val CHANNEL_NAME = "Inu Events"
        private const val CHANNEL_DESCRIPTION = "Inu Events 를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}