package com.xl4998.piggy.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper methods for time related logic
 */
@SuppressLint("SimpleDateFormat")
object TimeHelpers {

    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))

    init {
        sdf.timeZone = TimeZone.getTimeZone("America/New_York")
    }

    /**
     * Formats a Date object to a specified format
     */
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    /**
     * Returns current time as a Date object
     */
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}