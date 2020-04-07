package com.xl4998.piggy.ui.subscriptions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xl4998.piggy.data.db.SubscriptionRepository
import com.xl4998.piggy.data.db.entities.Subscription
import kotlinx.coroutines.*

/**
 * All UI related data for the corresponding view
 */
class SubscriptionsViewModel (private val subscriptionRepository: SubscriptionRepository) : ViewModel() {

    // Data
    private var allSubs: MutableList<Subscription> = mutableListOf() // Stores all subscriptions until it is ready to be notified by the Observer
    var liveAllSubs: MutableLiveData<MutableList<Subscription>> = MutableLiveData() // Actual object to notify

    // Refer to https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        // Grab all subscriptions for the user
        getAllSubs()
    }

    /**
     * Cancels all coroutines when this ViewModel is cleared to prevent memory leaks
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Grabs all subscriptions in the background
     */
    private fun getAllSubs() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                allSubs = subscriptionRepository.getAllSubs().toMutableList()
                liveAllSubs.postValue(allSubs)

                // TODO: Update UI
            }
        }
    }

    /**
     * Grab a subscription by name
     */
    private fun getSubByName(name: String): Subscription? {
        var sub: Subscription? = null

        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Get sub
                sub = subscriptionRepository.getSubByName(name)
            }
        }

        return sub
    }

    // TODO: other db related queries
}
