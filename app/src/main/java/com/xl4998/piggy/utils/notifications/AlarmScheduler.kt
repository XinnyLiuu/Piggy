package com.xl4998.piggy.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xl4998.piggy.data.db.entities.Subscription
import java.util.*

class AlarmScheduler {

    /**
     * Creates pending intent with subscription related data
     */
    fun createPendingIntent(
        context: Context,
        sub: Subscription
    ): PendingIntent? {
        // Create the intent
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
            .setAction("Subscription Reminder")
            .putExtra("name", sub.name)

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Schedule the alarm
     */
    fun scheduleAlarm(
        alarmManager: AlarmManager,
        alarmIntent: PendingIntent,
        sub: Subscription
    ) {
        // TODO: Calculate the time based on the user provided intervals

        // Prepare the time to start notify
        val datetimeToAlarm = Calendar.getInstance(Locale.getDefault())
        datetimeToAlarm.timeInMillis = System.currentTimeMillis()
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, 19)
        datetimeToAlarm.set(Calendar.MINUTE, 12)

        // Set repeating alarms
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, // Fire pending intent
            datetimeToAlarm.timeInMillis, // Alarm start
            (1000 * 60 * 60 * 24 * 7).toLong(), // Repeats every 7 days
            alarmIntent // Pending intent
        )
    }
}