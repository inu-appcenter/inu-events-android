package org.inu.events.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.viewmodel.DetailViewModel
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_CHANNEL_NAME = "UniLetter"
    }

    private lateinit var title: String
    private var  eventId :Int = -1

    override fun onReceive(context: Context, intent: Intent) {

        getIntentValue(intent)
        createNotificationChannel(context)
        notifyNotification(context)
    }

    private fun getIntentValue(intent: Intent) {
        title = intent.getStringExtra("title to receiver")!!
        eventId = intent.getIntExtra("eventId to receiver",-1)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    @SuppressLint("NewApi")
    private fun notifyNotification(context: Context) {
        //  todo 알람 클릭 처리
        val resultIntent = Intent(context, DetailActivity::class.java).apply {
        //  flags =  Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(EVENT_ID,eventId)
        }
          val pendingIntent: PendingIntent =
          PendingIntent.getActivity(context, LocalDateTime.now().nano, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText("행사 5분 전입니다.!!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notify(LocalDateTime.now().nano, build.build())

        }
    }
    // todo 알림이 울리고 난 뒤 (1회성 이기에 이렇게만 해두어도 되지 않을까?), shared remove 까지 해줄까?
    private fun afterNotification(){
        DetailViewModel().loadOnOffButton(false)
    }
}
