package com.xl4998.piggy.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xl4998.piggy.data.db.PiggyDatabase
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.utils.constants.ExpenseCategories
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
                        sub.cost.toInt()
                    )

                    // Add the subscription as an expense
                    val expense = Expense(
                        null,
                        ExpenseCategories.SUBSCRIPTION,
                        sub.name,
                        sub.cost,
                        sub.nextPaymentDate,
                        ""
                    )

                    db.expenseDao().insert(expense)

//                    // Update subscription's next payment date
//                    sub.nextPaymentDate
                }
            }
        }
    }
}