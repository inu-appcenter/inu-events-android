package org.inu.events.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.inu.events.R
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_CHANNEL_NAME = "UniLetter"
    }
    private lateinit var title :String


    override fun onReceive(context: Context, intent: Intent) {

        getIntentValue(intent)
        createNotificationChannel(context)
        notifyNotification(context)
    }

    private fun getIntentValue(intent:Intent){
        title =  intent.getStringExtra("value to receiver")!!
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
        //  val resultIntent = Intent(context, MainActivity::class.java).apply {
        //  flags =  Intent.FLAG_ACTIVITY_CLEAR_TASK
        //  flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //  }
        //  val pendingIntent: PendingIntent =
        //  PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                //      .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText("새우버거 가져가세요~!!.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)


            notify(LocalDateTime.now().nano, build.build())
        }
    }
}
