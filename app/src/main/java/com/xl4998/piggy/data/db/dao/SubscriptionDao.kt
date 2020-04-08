package com.xl4998.piggy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Represents the object used to interact with the subscription db entity
 */
@Dao
interface SubscriptionDao : BaseDao<Subscription> {
    @Query("select * from subscriptions")
    fun getAllSubs(): List<Subscription>

    @Query("select * from subscriptions where name = :name")
    fun getSubByName(name: String): Subscription

    @Query("update subscriptions set name = :newName, cost = :cost, date_subscribed = :date, interval = :interval where name = :oldName")
    fun updateByName(
        newName: String,
        oldName: String,
        cost: Double,
        date: String,
        interval: Int
    ): Int
}