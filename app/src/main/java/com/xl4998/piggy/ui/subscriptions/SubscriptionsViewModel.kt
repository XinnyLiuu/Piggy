package com.xl4998.piggy.ui.subscriptions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xl4998.piggy.data.db.SubscriptionRepository
import com.xl4998.piggy.data.db.entities.Subscription
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
            }
        }
    }
}
