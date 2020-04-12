package com.xl4998.piggy.data.db

import android.app.Application
import com.xl4998.piggy.data.db.entities.Subscription
import com.xl4998.piggy.utils.TimeHelper
import java.util.*

/**
 * Handles all subscription related data operations
 *
 * TODO: Handle exceptions
 */
class SubscriptionRepository(
    val application: Application
) {

    // Prepare database instance
    private val db: PiggyDatabase = PiggyDatabase.getInstance(application)
    private val subscriptionDao = db.subDao()

    // TimeHelper
    private val timeHelper = TimeHelper()

    /**
     * Returns all subscriptions the user has added
     */
    fun getAllSubs(): MutableList<Subscription> {
        return subscriptionDao.getAllSubs().toMutableList()
    }

    fun getSubByName(name: String): Subscription {
        return subscriptionDao.getSubByName(name)
    }

    /**
     * Inserts a new subscription into the database
     */
    fun addSubscription(sub: Subscription) {
        // Calculate next payment date, set the next payment field based on the specified interval
        val subDate = timeHelper.sdf.parse(sub.dateSubscribed) as Date
        sub.dateSubscribed = timeHelper.sdf.format(subDate)
        timeHelper.cal.time = subDate
        timeHelper.cal.add(Calendar.MONTH, sub.interval)
        sub.nextPaymentDate = timeHelper.sdf.format(timeHelper.cal.time)

        return subscriptionDao.insert(sub)
    }

    /**
     * Remove a subscription
     */
    fun removeSubscription(sub: Subscription): Int {
        return subscriptionDao.delete(sub)
    }

    /**
     * Update a subscription
     */
    fun updateSubscription(sub: Subscription, oldName: String): Int {
        // Calculate next payment date, set the next payment field based on the specified interval
        val subDate = timeHelper.sdf.parse(sub.dateSubscribed) as Date
        timeHelper.cal.time = subDate
        timeHelper.cal.add(Calendar.MONTH, sub.interval)

        sub.nextPaymentDate = timeHelper.sdf.format(timeHelper.cal.time)

        return subscriptionDao.updateByName(
            sub.name,
            oldName,
            sub.cost,
            sub.dateSubscribed,
            sub.interval,
            sub.nextPaymentDate
        )
    }
}