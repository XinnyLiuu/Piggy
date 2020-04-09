package com.xl4998.piggy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xl4998.piggy.data.db.dao.ExpenseDao
import com.xl4998.piggy.data.db.dao.SubscriptionDao
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * Room abstracted layer for interaction with SQLite
 */
@Database(entities = [Subscription::class, Expense::class], version = 1, exportSchema = false)
abstract class PiggyDatabase : RoomDatabase() {

    // DAO
    abstract fun subDao(): SubscriptionDao
    abstract fun expenseDao(): ExpenseDao

    // Singleton
    companion object {
        private var INSTANCE: PiggyDatabase? = null

        /**
         * Gets the single instance of PiggyDatabase
         */
        @Synchronized
        fun getInstance(context: Context): PiggyDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        PiggyDatabase::class.java,
                        "piggy.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE!!
        }
    }
}