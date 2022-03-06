package org.inu.events.common.util

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

    fun startTimeEndTime(): Boolean{
        if(startDate.value == endDate.value){
            val timeDiff = endDateTime.compareTo(startDateTime)
            if(timeDiff < 0 ) return true
        }
        return false
    }
}