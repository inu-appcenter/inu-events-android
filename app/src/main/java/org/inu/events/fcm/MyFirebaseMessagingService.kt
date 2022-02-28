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
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.inu.events.MainActivity
import org.inu.events.R
import org.inu.events.common.db.SharedPreferenceWrapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyFirebaseMessagingService:  FirebaseMessagingService(),KoinComponent{
    val context : Context by inject()
    private val db = SharedPreferenceWrapper(context)

    override fun onNewToken(p0: String) {
        Log.i("FCM TOKEN : ", p0)

        db.putString("fcmToken",p0)

        super.onNewToken(p0)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["body"]


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
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(this,R.color.primary100))
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
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

    companion object {
        private const val CHANNEL_NAME = "Inu Events"
        private const val CHANNEL_DESCRIPTION = "Inu Events 를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}