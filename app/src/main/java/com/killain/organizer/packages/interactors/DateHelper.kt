package com.killain.organizer.packages.interactors

import com.killain.organizer.packages.enums.FormatDateOutput

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DateHelper {

    private val day: String? = null
    private val month: String? = null
    private var result: String? = null
    private var time: String? = null
    private var date: String? = null
    private var task_date: String? = null
    private var minute: String? = null
    private var hour: String? = null
    private val int_hour: Int = 0
    private val int_minute: Int = 0
    private var localDate: LocalDate? = null
    private val zeroChar: Char = 0.toChar()

    val fullDateWithTime: String
        get() {
            date = localDateToString(localDate, FormatDateOutput.DEFAULT)
            result = "$date $time"
            return result as String
        }

    val year: Int?
        get() = localDate?.year

    val dayOfMonth: Int?
        get() = localDate?.dayOfMonth

    val long: Long?
        get() = localDate?.toEpochDay()

    init {
        localDate = LocalDate.now()
    }

    private fun formatMonth(month: Int?): String? {
        var convertedMonth: String? = null
        when (month) {
            1 -> convertedMonth = "Jan"

            2 -> convertedMonth = "Feb"

            3 -> convertedMonth = "Mar"

            4 -> convertedMonth = "Apr"

            5 -> convertedMonth = "May"

            6 -> convertedMonth = "Jun"

            7 -> convertedMonth = "Jul"

            8 -> convertedMonth = "Aug"

            9 -> convertedMonth = "Sep"

            10 -> convertedMonth = "Oct"

            11 -> convertedMonth = "Nov"

            12 -> convertedMonth = "Dec"
        }
        return convertedMonth
    }

    fun localDateToString(localDate: LocalDate?, formatDateOutput: FormatDateOutput): String {
        val compare = LocalDate.now()

        return if (compare.year == localDate?.year &&
                compare.monthValue == localDate.monthValue &&
                compare.dayOfMonth == localDate.dayOfMonth &&
                formatDateOutput === FormatDateOutput.FORMAT_DATE_OUTPUT) {
            result = "Today"
            result as String
        } else {
            val dow = convertLocalDateDayOfWeek(localDate?.dayOfWeek)
            val dayOfMonth = Integer.toString(localDate!!.dayOfMonth)
            val month = formatMonth(localDate.monthValue)
            val year = Integer.toString(localDate.year)
            result = "$dow, $month $dayOfMonth, $year"
            result as String
        }
    }

    fun localDateToTaskString(localDate: LocalDate): String {
        val year = Integer.toString(localDate.year)
        val month = Integer.toString(localDate.monthValue)
        val day = Integer.toString(localDate.dayOfMonth)
        task_date = "$day/$month/$year"
        return task_date as String
    }

    fun setDate(year: Int, month: Int, day: Int) {
        localDate = LocalDate.of(year, month, day)
    }

    fun setTime(hour: Int, minute: Int) {
        this.hour = Integer.toString(hour)
        this.minute = Integer.toString(minute)

        if (minute < 9) {
            this.minute = "0" + Integer.toString(minute)
        }

        if (hour < 9) {
            this.hour = "0" + Integer.toString(hour)
        }

        time = this.hour + ":" + this.minute
    }

    fun getMonth(): Int? {
        return localDate!!.monthValue
    }

    fun getConvertedDateFromLong(long_date: Long?): String {
        if (long_date != null) {
            localDate = LocalDate.ofEpochDay(long_date)
        }
        return localDateToString(localDate, FormatDateOutput.FORMAT_DATE_OUTPUT)
    }

    private fun convertLocalDateDayOfWeek(dayOfWeek: DayOfWeek?): String? {

        var converted_dow: String? = null

        when (dayOfWeek) {

            DayOfWeek.MONDAY -> converted_dow = "Mon"

            DayOfWeek.TUESDAY -> converted_dow = "Tue"

            DayOfWeek.WEDNESDAY -> converted_dow = "Wed"

            DayOfWeek.THURSDAY -> converted_dow = "Thu"

            DayOfWeek.FRIDAY -> converted_dow = "Fri"

            DayOfWeek.SATURDAY -> converted_dow = "Sat"
            DayOfWeek.SUNDAY -> converted_dow = "Sun"

            else -> {
            }
        }
        return converted_dow
    }

    fun setLocalDate(localDate: LocalDate) {
        this.localDate = localDate
    }
}
