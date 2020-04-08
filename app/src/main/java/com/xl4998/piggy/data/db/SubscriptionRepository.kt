package com.xl4998.piggy.data.db

import android.app.Application
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Handles all subscription related data operations
 *
 * TODO: Handle exceptions
 */
class SubscriptionRepository(application: Application) {

    // Prepare database instance
    private val db: PiggyDatabase = PiggyDatabase.getInstance(application)
    private val subscriptionDao = db.subDao()

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

    /**
     * Inserts a new subscription into the database
     */
    fun addSubscription(sub: Subscription) {
        return subscriptionDao.insert(sub)
    }

    /**
     * Remove a subscription
     */
    fun removeSubscription(sub: Subscription): Int {
        return subscriptionDao.delete(sub)
    }
}