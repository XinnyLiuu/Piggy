package com.xl4998.piggy.data.db

import android.app.Application
import android.util.Log
import com.xl4998.piggy.data.db.dao.ExpenseDao
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.utils.TimeHelpers.cal
import com.xl4998.piggy.utils.TimeHelpers.getCurrentDateTime
import com.xl4998.piggy.utils.TimeHelpers.sdf
import com.xl4998.piggy.utils.TimeHelpers.toString
import java.util.*

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
    fun getExpensesThisMonth(): MutableList<Expense> {
        val expenses = expenseDao.getAllExpenses()

        val currDateStr = getCurrentDateTime().toString("yyyy-MM-dd")
        val currDate = sdf.parse(currDateStr)
        cal.time = currDate!!

        // Find all expenses whose Year and Month matches this month
        val currMonth = cal.get(Calendar.MONTH)
        val currYear = cal.get(Calendar.YEAR)

        // Return all the dates whose month matches this month and year
        return expenses.filter {
            val expenseDate = sdf.parse(it.date)
            cal.time = expenseDate!!
            val expenseMonth = cal.get(Calendar.MONTH)
            val expenseYear = cal.get(Calendar.YEAR)

            expenseMonth == currMonth && expenseYear == currYear
        }.toMutableList()
    }

    /**
     * Returns all expenses for last month
     */
    fun getExpensesLastMonth(): MutableList<Expense> {
        val expenses = expenseDao.getAllExpenses()

        val currDateStr = getCurrentDateTime().toString("yyyy-MM-dd")
        val currDate = sdf.parse(currDateStr)
        cal.time = currDate!!

        // Find all expenses whose Year and Month matches last month
        cal.add(Calendar.MONTH, -1)
        val lastMonth = cal.get(Calendar.MONTH)
        val lastMonthYear = cal.get(Calendar.YEAR)

        // Return all the dates whose month matches last month
        return expenses.filter {
            val expenseDate = sdf.parse(it.date)
            cal.time = expenseDate!!
            val expenseMonth = cal.get(Calendar.MONTH)
            val expenseYear = cal.get(Calendar.YEAR)

            expenseMonth == lastMonth && expenseYear == lastMonthYear
        }.toMutableList()
    }

    /**
     * Returns all expenses from the last 6 months
     */
    fun getExpensesLastSixMonths(): MutableList<Expense> {
        val expenses = expenseDao.getAllExpenses()

        val currDateStr = getCurrentDateTime().toString("yyyy-MM-dd")

        // Find all expenses whose Year and Month matches last 6 months
        val currDate = sdf.parse(currDateStr)

        cal.time = currDate!!
        cal.add(Calendar.MONTH, -6)
        val sixMonthsAgoDate = cal.time

        // Return all the dates whose month matches last 6 months month
        return expenses.filter {
            val expenseDate = sdf.parse(it.date)

            !expenseDate!!.before(sixMonthsAgoDate) && !expenseDate.after(currDate)
        }.toMutableList()
    }

    /**
     * Returns all expenses for the year
     */
    fun getExpensesThisYear(): MutableList<Expense> {
        val expenses = expenseDao.getAllExpenses()

        val currDateStr = getCurrentDateTime().toString("yyyy-MM-dd")
        val currDate = sdf.parse(currDateStr)
        cal.time = currDate!!

        // Find all expenses whose Year and Month matches this year
        val currYear = cal.get(Calendar.YEAR)

        // Return all the dates whose month matches this month
        return expenses.filter {
            val expenseDate = sdf.parse(it.date)
            cal.time = expenseDate!!
            val expenseYear = cal.get(Calendar.YEAR)

            expenseYear == currYear
        }.toMutableList()
    }

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