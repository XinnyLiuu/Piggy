package com.xl4998.piggy.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xl4998.piggy.data.db.PiggyDatabase
import com.xl4998.piggy.data.db.SubscriptionRepository
import com.xl4998.piggy.utils.TimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check the intent
        if (intent!!.action!! == "Subscription Reminder") {

            // Get the name stored in intent
            val uniqueName = intent.extras!!.getString("name")

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val db: PiggyDatabase = PiggyDatabase.getInstance(context!!.applicationContext)

                    // Get the subscription
                    val sub = db.subDao().getAllSubs().filter {
                        it.name == uniqueName
                    }.toList()[0]

                    // Show notification for subscription
                    NotificationHelper().showNotification(
                        context,
                        "%s Subscription Reminder".format(sub.name),
                        "The payment for %s is due on %s".format(sub.name, sub.nextPaymentDate),
                        TimeHelper().getNotificationUniqueID()
                    )
                }
            }
        }

        if (intent.action!! == "Subscription Update") {

            // Get the name stored in intent
            val uniqueName = intent.extras!!.getString("name")

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val db: PiggyDatabase = PiggyDatabase.getInstance(context!!.applicationContext)

                    // Get the subscription
                    var sub = db.subDao().getAllSubs().filter {
                        it.name == uniqueName
                    }.toList()[0]

                    // Update the subscription which will update the next payment date as well
                    sub = SubscriptionRepository.prepareSub(sub)

                    db.subDao().updateByName(
                        sub.name,
                        sub.name,
                        sub.cost,
                        sub.dateSubscribed,
                        sub.interval,
                        sub.nextPaymentDate
                    )

                    // Prepare new intents
                    val alarmScheduler = AlarmScheduler()
                    val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    // Delete old pending intents
                    var pendingIntent: PendingIntent = alarmScheduler.createSubReminderIntent(context.applicationContext, sub) as PendingIntent
                    pendingIntent.cancel()
                    pendingIntent = alarmScheduler.createSubUpdateIntent(context.applicationContext, sub)
                    pendingIntent.cancel()

                    // Set an alarm to notify about the subscription payment date a week before
                    pendingIntent = alarmScheduler.createSubReminderIntent(context.applicationContext, sub) as PendingIntent
                    alarmScheduler.scheduleAlarmOneWeekBeforeSubDate(alarmManager, pendingIntent, sub)

                    // Set an alarm to update the subscription payment date on the day of the payment
                    pendingIntent = alarmScheduler.createSubUpdateIntent(context.applicationContext, sub)
                    alarmScheduler.scheduleAlarmDayOfSubDate(alarmManager, pendingIntent, sub)

                    // Show notification for subscription
                    NotificationHelper().showNotification(
                        context,
                        "%s Subscription is Due".format(sub.name),
                        "Remember to pay your subscription ASAP!".format(
                            sub.name,
                            sub.nextPaymentDate
                        ),
                        TimeHelper().getNotificationUniqueID()
                    )
                }
            }
        }
    }
}