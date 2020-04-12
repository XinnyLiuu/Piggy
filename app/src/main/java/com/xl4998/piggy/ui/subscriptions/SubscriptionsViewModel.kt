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
    fun getAllSubs() {
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

//                // Get the new sub
//                val newSub = subscriptionRepository.getSubByName(sub.name)
//
//                // Set an alarm to notify about the subscription payment date a week before
//                val alarmScheduler = AlarmScheduler()
//                val scheduledAlarm = alarmScheduler.createPendingIntent(
//                    subscriptionRepository.application,
//                    newSub
//                ) as PendingIntent
//                val alarmManager =
//                    subscriptionRepository.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                alarmScheduler.scheduleAlarm(alarmManager, scheduledAlarm, newSub)
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

//                // Get the updated sub
//                val updatedSub = subscriptionRepository.getSubByName(sub.name)
//
//                // Set an alarm to notify about the subscription payment date a week before
//                val alarmScheduler = AlarmScheduler()
//                val scheduledAlarm = alarmScheduler.createPendingIntent(
//                    subscriptionRepository.application,
//                    updatedSub
//                ) as PendingIntent
//                val alarmManager =
//                    subscriptionRepository.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                alarmScheduler.scheduleAlarm(alarmManager, scheduledAlarm, updatedSub)
            }
        }
    }
}
