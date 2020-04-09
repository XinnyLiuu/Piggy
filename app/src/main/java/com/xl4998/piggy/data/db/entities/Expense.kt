package com.xl4998.piggy.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an expense entered by the user
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "cost") val cost: Double
)