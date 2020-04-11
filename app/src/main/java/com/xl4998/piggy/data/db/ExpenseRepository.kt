package com.xl4998.piggy.data.db

import android.app.Application
import com.xl4998.piggy.data.db.dao.ExpenseDao
import com.xl4998.piggy.data.db.entities.Expense

/**
 * Handles all expense related data operations
 *
 * TODO: Handle exceptions
 */
class ExpenseRepository(application: Application) {

    // Prepare database instance
    private val db: PiggyDatabase = PiggyDatabase.getInstance(application)
    private val expenseDao: ExpenseDao = db.expenseDao()

    /**
     * Returns all expenses the user has added
     */
    fun getAllExpenses(): List<Expense> {
        return expenseDao.getAllExpenses()
    }

    /**
     * Returns the Expense object based on provided id
     */
    fun getExpenseById(id: Long): Expense {
        return expenseDao.getExpenseById(id)
    }

    /**
     * Returns all expenses for this month
     */
//    fun getExpensesThisMonth(): List<Expense> {
//        val expenses = expenseDao.getAllExpenses()
//    }
//
//    /**
//     * Returns all expenses for last month
//     */
//    fun getExpensesLastMonth(): List<Expense> {
//
//    }
//
//    /**
//     * Returns all expenses from the last 6 months
//     */
//    fun getExpensesLastSixMonths(): List<Expense> {
//
//    }
//
//    /**
//     * Returns all expenses for the year
//     */
//    fun getExpensesThisYear(): List<Expense> {
//
//    }

    /**
     * Inserts a new expense into the database
     */
    fun addExpense(expense: Expense) {
        return expenseDao.insert(expense)
    }

    /**
     * Remove an expense
     */
    fun removeExpense(expense: Expense): Int {
        return expenseDao.delete(expense)
    }

    /**
     * Update an expense
     */
    fun updateExpense(expense: Expense): Int {
        return expenseDao.update(expense)
    }
}