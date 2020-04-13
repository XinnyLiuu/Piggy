package com.xl4998.piggy.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper methods for time related logic
 */
@SuppressLint("SimpleDateFormat")
class TimeHelper {

    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"))

    init {
        sdf.timeZone = TimeZone.getTimeZone("America/New_York")
    }

    /**
     * Returns current time as a Date object
     */
    fun getCurrentDateTimeStr(): String {
        val currDate = cal.time
        return sdf.format(currDate)
    }

    /**
     * Creates an unique notification id using the current time
     */
    fun getNotificationUniqueID(): Int {
        return cal.timeInMillis.toInt()
    }
}