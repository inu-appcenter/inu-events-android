package org.inu.events.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.legacy.content.WakefulBroadcastReceiver
import com.google.android.gms.stats.WakeLock
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.common.util.WakeLockUtil
import org.inu.events.data.model.dto.AlarmDisplayModel
import org.inu.events.objects.IntentMessage.BACK_FROM_ALARM
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

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        // todo 디바이스 재부팅시 알람 꺼짐 해결

        WakeLockUtil().acquireCpuWakeLock(context)

        context.startService(Intent(context,AlarmService::class.java))

        getIntentValue(intent)
        createNotificationChannel(context)
        notifyNotification(context)
        afterNotification(context)
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
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            flags =  Intent.FLAG_ACTIVITY_CLEAR_TASK
//            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            putExtra(EVENT_ID,eventId)
            putExtra(BACK_FROM_ALARM, true)

        }
        val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, LocalDateTime.now().nano, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText("행사 5분 전입니다.!!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notify(LocalDateTime.now().nano, build.build())

        }
    }
    // todo 알림이 울리고 난 뒤 shared remove 까지 해줄까?
    private fun afterNotification(context: Context){
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent.cancel()
    }
}
