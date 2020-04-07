package com.xl4998.piggy.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Interface for methods that all children will inherit
 */
interface BaseDao<T> {
    @Insert
    fun insert(t: T)

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)
}