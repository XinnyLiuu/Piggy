package com.xl4998.piggy.data.db

import com.xl4998.piggy.data.db.dao.SubscriptionDao
import com.xl4998.piggy.data.db.entities.Subscription
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles all subscription related data operations
 */
@Singleton
class SubscriptionRepository @Inject constructor(
    private val subscriptionDao: SubscriptionDao
) {
    /**
     * Returns all subscriptions the user has added
     */
    fun getAllSubs(): List<Subscription> {
        return subscriptionDao.getAllSubs()
    }

    /**
     * Returns the Subscription object based on provided name
     */
    fun getSubByName(name: String): Subscription {
        return subscriptionDao.getSubByName(name)
    }
}