package com.anless.rentmotors.utils

import java.util.*
import java.text.SimpleDateFormat

object DateFormatter {
    private fun getFormattedDate(calendar: Calendar, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(calendar.timeInMillis)
    }

    fun formatRentDate(calendar: Calendar): String {
        return getFormattedDate(calendar, "dd MMM yyyy")
    }

    fun formatRentDateTime(calendar: Calendar): String {
        return getFormattedDate(calendar, "dd MMMM yyyy, HH:mm")
    }

    fun formatRentTime(calendar: Calendar): String {
        return getFormattedDate(calendar, "HH:mm")
    }

    fun formatRentDayOfWeek(calendar: Calendar): String {
        return getFormattedDate(calendar, "EEEE").replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    fun formatRentDateToServer(calendar: Calendar): String {
        return getFormattedDate(calendar, "yyyyMMddHHmm")
    }

    fun formatDocumentsDateToServer(date: Calendar?): String {
        return if (date != null) {
            var dateString = date.get(Calendar.YEAR).toString()
            dateString += String.format("%02d", date.get(Calendar.MONTH))
            dateString += String.format("%02d", date.get(Calendar.DAY_OF_MONTH))
            dateString
        } else {
            ""
        }
    }
}