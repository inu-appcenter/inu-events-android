package org.inu.events

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.service.AlarmReceiver
import java.util.*
import kotlin.collections.ArrayList

class AlarmForm(val context: Context, val eventID: Int) {
    fun registerAlarmOnDetail(title:String, date:String, isBeforeStart:Boolean) {
        val calendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(date)
        }
        val db = SharedPreferenceWrapper(context)
        val idArrayList = getArrayList("id",eventID).apply {
            add(eventID)
        }
        val titleArrayList = getArrayList("title","").apply {
            add(title)
        }
        val dateArrayList = getArrayList("date","").apply {
            add(date)
        }
        val isBeforeStartArrayList = getArrayList("isBeforeStart",false).apply {
            add(isBeforeStart)
        }

        db.putArrayInt("id",idArrayList.toTypedArray())
        db.putArrayString("title",titleArrayList.toTypedArray())
        db.putArrayString("date",dateArrayList.toTypedArray())
        db.putArrayBoolean("isBeforeStart",isBeforeStartArrayList.toTypedArray())

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("eventId to receiver", eventID)
            putExtra("title to receiver", title)
            putExtra("isBeforeStart to receiver", isBeforeStart)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            // 이렇게하면 계속 쌓이기에 ONOFF_KEY 로 하면 각 eventId에 맞게 업데이트
            context, eventID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    fun cancelAlarm(intent: Intent = Intent(context, AlarmReceiver::class.java)) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()

        val db = SharedPreferenceWrapper(context)
        var idx :Int = -1
        val idArrayList = getArrayList("id",eventID).apply {
            idx = this.indexOf(eventID)
            removeAt(idx)
        }
        val dateArrayList = getArrayList("date","").apply {
            removeAt(idx)
        }

        val titleArrayList = getArrayList("title","").apply {
            removeAt(idx)
        }

        val isBeforeStartArrayList = getArrayList("isBeforeStart",false).apply {
            removeAt(idx)
        }

        db.putArrayInt("id",idArrayList.toTypedArray())
        db.putArrayString("title",titleArrayList.toTypedArray())
        db.putArrayString("date",dateArrayList.toTypedArray())
        db.putArrayBoolean("isBeforeStart",isBeforeStartArrayList.toTypedArray())
    }

    inline fun <reified T> getArrayList(key: String, value:T): java.util.ArrayList<T> {
        val db = SharedPreferenceWrapper(context)
        lateinit var array: Array<T>
        if (key == "id" ){
            array = db.getArrayInt("id")?.let {
                it as Array<T>
            } ?: arrayOf()
        }
        else if (key == "date"){
            array = db.getArrayString("date")?.let {
                it as Array<T>
            } ?: arrayOf()
        }
        else if(key == "title"){
            array = db.getArrayString("title")?.let {
                it as Array<T>
            } ?: arrayOf()
        }
        else{
            array = db.getArrayBoolean("isBeforeStart")?.let {
                it as Array<T>
            } ?: arrayOf()
        }

        val arrayList = arrayListOf<T>()
        array.let{
            for (id in it)
                arrayList.add(id)
        }
        return arrayList
    }
}
