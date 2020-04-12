package com.xl4998.piggy.utils.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.xl4998.piggy.data.db.PiggyDatabase
import com.xl4998.piggy.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Listens to boot related intents
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(
            context,
            "Device Booted up! Piggy is setting scheduled reminders",
            Toast.LENGTH_LONG
        ).show()

        // Set scheduled reminders for each subscription
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                // Grab subscription data
                val db: PiggyDatabase = PiggyDatabase.getInstance(context!!.applicationContext)
                val subs = db.subDao().getAllSubs()

                // Create notification channel
                NotificationHelper.createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_HIGH,
                    false,
                    "Piggy Subscriptions",
                    "Subscriptions"
                )

                // TODO: Start notification for each sub
                var count = 0
                for (sub in subs) {
                    count += 1

                    // TODO: Schedule intents for notification reminders
                    NotificationHelper.showNotification(context, sub.name, sub.cost.toString(), count)
                }
            }
        }
    }
}