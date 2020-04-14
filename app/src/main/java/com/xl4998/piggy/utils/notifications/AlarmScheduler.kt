package com.xl4998.piggy.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xl4998.piggy.data.db.entities.Subscription
import com.xl4998.piggy.utils.TimeHelper
import java.util.*

class AlarmScheduler {

    /**
     * Creates pending intent for upcoming subscription payment
     */
    fun createSubReminderIntent(
        context: Context,
        sub: Subscription
    ): PendingIntent? {
        // Create the intent
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
            .setAction("Subscription Reminder")
            .setType("%s Payment Reminder".format(sub.name))
            .putExtra("name", sub.name)

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Creates pending intent to update a subscription payment date
     */
    fun createSubUpdateIntent(
        context: Context,
        sub: Subscription
    ): PendingIntent {
        // Create the intent
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
            .setAction("Subscription Update")
            .setType("%s Update".format(sub.name))
            .putExtra("name", sub.name)

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Schedule the alarm one week before the due date
     */
    fun scheduleAlarmOneWeekBeforeSubDate(
        alarmManager: AlarmManager,
        alarmIntent: PendingIntent,
        sub: Subscription
    ) {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Prepare the time to start notify
        val subPaymentDate = timeHelper.sdf.parse(sub.nextPaymentDate) as Date
        timeHelper.cal.time = subPaymentDate
        timeHelper.cal.add(Calendar.DAY_OF_MONTH, -7) // Set time of start to 7 days before

        // Set alarm
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, // Fire pending intent
            timeHelper.cal.time.time, // Alarm start
            alarmIntent // Pending intent
        )
    }

    /**
     * Schedule the alarm on the due date
     */
    fun scheduleAlarmDayOfSubDate(
        alarmManager: AlarmManager,
        alarmIntent: PendingIntent,
        sub: Subscription
    ) {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Prepare the time to start notify
        val subPaymentDate = timeHelper.sdf.parse(sub.nextPaymentDate) as Date
        timeHelper.cal.time = subPaymentDate

        // Set alarm
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, // Fire pending intent
            timeHelper.cal.time.time, // Alarm start
            alarmIntent // Pending intent
        )
    }
}