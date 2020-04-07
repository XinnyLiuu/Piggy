package com.xl4998.piggy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xl4998.piggy.data.db.dao.SubscriptionDao
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Room abstracted layer for interaction with SQLite
 */
@Database(entities = [Subscription::class], version = 1)
abstract class PiggyDatabase : RoomDatabase() {
    abstract fun subDao(): SubscriptionDao
}