package com.xl4998.piggy.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Interface for methods that all children will inherit
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(t: T)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(t: T)

    @Delete
    fun delete(t: T)
}