package org.inu.events.common.util

import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Period {
    val startDatePeriod = MutableLiveData("")
    val startTimePeriod = MutableLiveData("")
    val endDatePeriod = MutableLiveData("")
    val endTimePeriod = MutableLiveData("")

    fun serverDateToString(date: String): String{
        val stringDate: LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return stringDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

    fun serverTimeToString(time: String): String{
        val timeDate: LocalTime = LocalTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return timeDate.format(DateTimeFormatter.ofPattern("hh:mm a", Locale("en", "KO")))
    }

    fun formatDateForServer(date: String): String{
        val serverDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm a", Locale("en", "KO")))
        return serverDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}