package com.xl4998.piggy.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.xl4998.piggy.data.db.PiggyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Listens to boot related intents
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == Intent.ACTION_BOOT_COMPLETED) {

            // Show toast to signal this is working
            Toast.makeText(
                context,
                "Piggy is preparing subscription reminders",
                Toast.LENGTH_LONG
            ).show()

            // Set scheduled reminders for each subscription
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    // Grab subscription data
                    val db: PiggyDatabase = PiggyDatabase.getInstance(context!!.applicationContext)
                    val subs = db.subDao().getAllSubs()

                    // Create notification channel
                    val notificationHelper =
                        NotificationHelper()
                    notificationHelper.createNotificationChannel(
                        context,
                        NotificationManagerCompat.IMPORTANCE_HIGH,
                        false,
                        "Subscriptions",
                        "Subscription Notifications"
                    )

                    // Check the size of subs, if there are none, do not schedule notifications
                    if (subs.isNotEmpty()) {
                        // Alarm related
                        val alarmScheduler = AlarmScheduler()
                        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        // Schedule notification for subscriptions
                        for (sub in subs) {
                            // Set an alarm to notify about the subscription payment date a week before
                            var pendingIntent: PendingIntent = alarmScheduler.createSubReminderIntent(context, sub) as PendingIntent
                            alarmScheduler.scheduleAlarmOneWeekBeforeSubDate(alarmManager, pendingIntent, sub)

                            // Set an alarm to update the subscription payment date on the day of the payment
                            pendingIntent = alarmScheduler.createSubUpdateIntent(context, sub)
                            alarmScheduler.scheduleAlarmDayOfSubDate(alarmManager, pendingIntent, sub)
                        }
                    }
                }
            }
        }
    }
}