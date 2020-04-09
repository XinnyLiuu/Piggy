package com.xl4998.piggy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xl4998.piggy.data.db.entities.Expense

/**
 * Used by Room to interact with Expense entities
 */
@Dao
interface ExpenseDao : BaseDao<Expense> {
    @Query("select * from expenses")
    fun getAllExpenses(): List<Expense>

    @Query("select * from expenses where id = :id")
    fun getExpenseById(id: Long): Expense
}