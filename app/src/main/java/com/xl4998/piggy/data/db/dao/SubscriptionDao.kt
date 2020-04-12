package com.xl4998.piggy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Used by Room to interact with Subscription entities
 */
@Dao
interface SubscriptionDao : BaseDao<Subscription> {
    @Query("select * from subscriptions")
    fun getAllSubs(): List<Subscription>

    @Query("select * from subscriptions where name = :name")
    fun getSubByName(name: String): Subscription

    @Query("update subscriptions set name = :newName, cost = :cost, date_subscribed = :date, interval = :interval, next_payment_date = :nextPayment where name = :oldName")
    fun updateByName(
        newName: String,
        oldName: String,
        cost: Double,
        date: String,
        interval: Int,
        nextPayment: String
    ): Int
}