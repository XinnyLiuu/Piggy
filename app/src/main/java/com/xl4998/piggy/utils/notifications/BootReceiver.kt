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
                "Piggy is preparing scheduled reminders",
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
                        "Subscription Reminders"
                    )

                    // Check the size of subs, if there are none, do not schedule notifications
                    if (subs.isNotEmpty()) {
                        val alarmScheduler =
                            AlarmScheduler()

                        // Schedule notification reminders for subscriptions
                        for (sub in subs) {
                            val scheduledAlarm =
                                alarmScheduler.createPendingIntent(context, sub) as PendingIntent
                            val alarmManager =
                                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            alarmScheduler.scheduleAlarm(alarmManager, scheduledAlarm, sub)
                        }
                    }
                }
            }
        }
    }
}