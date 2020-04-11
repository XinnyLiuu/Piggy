package com.xl4998.piggy.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a subscription entered by the user
 */
@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "cost") var cost: Double,
    @ColumnInfo(name = "date_subscribed") var dateSubscribed: String, //// yyyy-MM-dd
    @ColumnInfo(name = "interval") var interval: Int // Interval should be used to recur expenses
)