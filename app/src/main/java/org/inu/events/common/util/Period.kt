package org.inu.events.common.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Period {
    lateinit var startDateTime: Date
    lateinit var endDateTime: Date

    var checkDeadline = false

    val startDate = MutableLiveData("")
    val startTime = MutableLiveData("")
    val endDate = MutableLiveData("")
    val endTime = MutableLiveData("")

    val formattedStartDateTime: String
        get() = datePickerToString(startDate.value!!, startTime.value!!)
    val formattedEndDateTime: String
        get() = datePickerToString(endDate.value!!, endTime.value!!)

    fun serverDateToString(date: String): String {
        val stringDate: LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return stringDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

    fun serverTimeToString(time: String): String {
        val timeDate: LocalTime = LocalTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return timeDate.format(DateTimeFormatter.ofPattern("hh:mm a", Locale("en", "KO")))
    }

    private fun formatDateForServer(date: String): String {
        val serverDate = LocalDateTime.parse(
            date,
            DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm a", Locale("en", "KO"))
        )
        return serverDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    fun datePickerToString(date: String, time: String): String {
        return formatDateForServer("$date $time")
    }

    fun formatDate(date: Date): String = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))
        .format(date)
        .toString()

    fun formatTime(date: Date): String = SimpleDateFormat("hh:mm a", Locale("en", "US"))
        .format(date)
        .toString()

    fun setStartDate(date: Date) {
        startDate.value = formatDate(date)
    }

    fun setStartTime(date: Date) {
        startDateTime = date
        startTime.value = formatTime(date)
    }

    fun setEndDate(date: Date) {
        endDate.value = formatDate(date)
    }

    fun setEndTime(date: Date) {
        endDateTime = date
        endTime.value = formatTime(date)
    }

    fun setupCurrentTime() {
        val date = Date()

        setStartDate(date)
        setStartTime(date)
        setEndDate(date)
        setEndTime(date)
    }

    fun startTimeEndTime(startAt:String?, endAt:String?): Boolean{
        if(startAt == null || endAt == null) return false
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val start = dateFormat.parse(startAt).time
        val end = dateFormat.parse(endAt).time


        val diff = (end - start)
        return when(diff<0){
            true -> true
            else -> false
        }
    }

    fun whenDay(end_at: String?, check:Boolean = false): String {
        if (end_at == null) return "D-??"

        var dateFormat = SimpleDateFormat("yyyy-MM-dd")

        if(check) dateFormat = SimpleDateFormat("yyyy.MM.dd")


        val endAt = dateFormat.parse(end_at).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val dDay = (endAt - today) / (24 * 60 * 60 * 1000)

        if (dDay < 0) {
            checkDeadline = true
            return "마감"
        }
        checkDeadline = false
        return "D-${if(dDay.toInt() == 0) "day" else dDay}"
    }
}