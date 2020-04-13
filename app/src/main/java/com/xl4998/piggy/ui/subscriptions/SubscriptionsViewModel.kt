package com.xl4998.piggy.ui.subscriptions

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xl4998.piggy.data.db.SubscriptionRepository
import com.xl4998.piggy.data.db.entities.Subscription
import com.xl4998.piggy.utils.notifications.AlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * All Subscriptions UI related data
 */
class SubscriptionsViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    // Data
    private var allSubs: MutableList<Subscription> = mutableListOf()
    var liveAllSubs: MutableLiveData<MutableList<Subscription>> = MutableLiveData()

    init {
        // Grab all subscriptions for the user
        getAllSubs()
    }

    /**
     * Grabs all subscriptions
     */
    private fun getAllSubs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allSubs = subscriptionRepository.getAllSubs()
                liveAllSubs.postValue(allSubs)
            }
        }
    }

    /**
     * Inserts a new subscription into the database
     */
    fun addNewSub(sub: Subscription) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Add the subscription
                subscriptionRepository.addSubscription(sub)
                allSubs = subscriptionRepository.getAllSubs()
                liveAllSubs.postValue(allSubs)

                // Get new sub
                val newSub = subscriptionRepository.getSubByName(sub.name)

                // Alarm related
                val alarmScheduler = AlarmScheduler()
                val alarmManager = subscriptionRepository.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                // Set an alarm to notify about the subscription payment date a week before
                var pendingIntent: PendingIntent = alarmScheduler.createSubReminderIntent(subscriptionRepository.application, newSub) as PendingIntent
                alarmScheduler.scheduleAlarmOneWeekBeforeSubDate(alarmManager, pendingIntent, newSub)

                // Set an alarm to update the subscription payment date on the day of the payment
                pendingIntent = alarmScheduler.createSubUpdateIntent(subscriptionRepository.application, newSub)
                alarmScheduler.scheduleAlarmDayOfSubDate(alarmManager, pendingIntent, newSub)
            }
        }
    }

    /**
     * Removes a stored subscription
     */
    fun removeSub(sub: Subscription) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Remove the subscription
                subscriptionRepository.removeSubscription(sub)
                allSubs = subscriptionRepository.getAllSubs()
                liveAllSubs.postValue(allSubs)

                // Alarm
                val alarmScheduler = AlarmScheduler()

                // Delete any pending intents the sub may have
                var pendingIntent: PendingIntent = alarmScheduler.createSubReminderIntent(subscriptionRepository.application, sub) as PendingIntent
                pendingIntent.cancel()
                pendingIntent = alarmScheduler.createSubUpdateIntent(subscriptionRepository.application, sub)
                pendingIntent.cancel()
            }
        }
    }

    /**
     * Update a stored subscription
     */
    fun updateSub(sub: Subscription, oldName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Update the subscription
                subscriptionRepository.updateSubscription(sub, oldName)
                allSubs = subscriptionRepository.getAllSubs()
                liveAllSubs.postValue(allSubs)

                // Get sub
                val newSub = subscriptionRepository.getSubByName(sub.name)

                // Save the names of the subscription
                val newName = newSub.name
                newSub.name = oldName

                // Alarm
                val alarmScheduler = AlarmScheduler()
                val alarmManager = subscriptionRepository.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                // Delete old pending intents
                var pendingIntent: PendingIntent = alarmScheduler.createSubReminderIntent(subscriptionRepository.application, newSub) as PendingIntent
                pendingIntent.cancel()
                pendingIntent = alarmScheduler.createSubUpdateIntent(subscriptionRepository.application, newSub)
                pendingIntent.cancel()

                // Set name as new name
                newSub.name = newName

                // Set an alarm to notify about the subscription payment date a week before
                pendingIntent = alarmScheduler.createSubReminderIntent(subscriptionRepository.application, newSub) as PendingIntent
                alarmScheduler.scheduleAlarmOneWeekBeforeSubDate(alarmManager, pendingIntent, newSub)

                // Set an alarm to update the subscription payment date on the day of the payment
                pendingIntent = alarmScheduler.createSubUpdateIntent(subscriptionRepository.application, newSub)
                alarmScheduler.scheduleAlarmDayOfSubDate(alarmManager, pendingIntent, newSub)
            }
        }
    }
}
