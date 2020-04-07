package com.xl4998.piggy.data.db

import android.app.Application
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Handles all subscription related data operations
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

    // TODO: other db related queries
}