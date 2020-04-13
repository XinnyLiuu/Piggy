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

    companion object {
        /**
         * Receives a subscription and prepares the dates
         */
        fun prepareSub(sub: Subscription): Subscription {
            // TimeHelper
            val timeHelper = TimeHelper()

            // Check if the subbed date is less the current date, if so we are going to calculate the next payment date based on the given interval to find the next closest possible date after the curr date
            val currDate = timeHelper.cal.time
            var subDate = timeHelper.sdf.parse(sub.dateSubscribed) as Date
            sub.dateSubscribed = timeHelper.sdf.format(subDate)

            timeHelper.cal.time = subDate

            if (subDate.before(currDate)) {
                while(subDate.before(currDate)) {
                    timeHelper.cal.add(Calendar.MONTH, sub.interval)
                    subDate = timeHelper.cal.time
                }
            } else {
                timeHelper.cal.add(Calendar.MONTH, sub.interval)
            }

            sub.nextPaymentDate = timeHelper.sdf.format(timeHelper.cal.time)

            return sub
        }
    }

    // Prepare database instance
    private val db: PiggyDatabase = PiggyDatabase.getInstance(application)
    private val subscriptionDao = db.subDao()

    /**
     * Returns all subscriptions the user has added
     */
    fun getAllSubs(): MutableList<Subscription> {
        return subscriptionDao.getAllSubs().toMutableList()
    }

    /**
     * Returns the sub by a given name
     */
    fun getSubByName(name: String): Subscription {
        return subscriptionDao.getSubByName(name)
    }

    /**
     * Inserts a new subscription into the database
     */
    fun addSubscription(sub: Subscription) {
        val preppedSub = prepareSub(sub)

        return subscriptionDao.insert(preppedSub)
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
        val preppedSub = prepareSub(sub)

        return subscriptionDao.updateByName(
            preppedSub.name,
            oldName,
            preppedSub.cost,
            preppedSub.dateSubscribed,
            preppedSub.interval,
            preppedSub.nextPaymentDate
        )
    }
}