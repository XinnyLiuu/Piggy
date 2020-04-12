package com.xl4998.piggy.data.db

import android.app.Application
import com.xl4998.piggy.data.db.dao.ExpenseDao
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.utils.TimeHelper
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
    fun getAllExpenses(): MutableList<Expense> {
        return expenseDao.getAllExpenses() as MutableList<Expense>
    }

    /**
     * Returns all expenses for this month
     */
    fun getExpensesThisMonth(): MutableList<Expense> {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Get all expenses
        val expenses = expenseDao.getAllExpenses()

        // Set curr dates
        val currDateStr = timeHelper.getCurrentDateTimeStr()
        val currDate = timeHelper.sdf.parse(currDateStr)
        timeHelper.cal.time = currDate!!

        // Find all expenses whose Year and Month matches this month
        val currMonth = timeHelper.cal.get(Calendar.MONTH)
        val currYear = timeHelper.cal.get(Calendar.YEAR)

        // Return all the dates whose month matches this month and year
        return expenses.filter {
            val expenseDate = timeHelper.sdf.parse(it.date)
            timeHelper.cal.time = expenseDate!!
            val expenseMonth = timeHelper.cal.get(Calendar.MONTH)
            val expenseYear = timeHelper.cal.get(Calendar.YEAR)

            expenseMonth == currMonth && expenseYear == currYear
        }.toMutableList()
    }

    /**
     * Returns all expenses for last month
     */
    fun getExpensesLastMonth(): MutableList<Expense> {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Get all expenses
        val expenses = expenseDao.getAllExpenses()

        // Set curr dates
        val currDateStr = timeHelper.getCurrentDateTimeStr()
        val currDate = timeHelper.sdf.parse(currDateStr)
        timeHelper.cal.time = currDate!!

        // Find all expenses whose Year and Month matches last month
        timeHelper.cal.add(Calendar.MONTH, -1)
        val lastMonth = timeHelper.cal.get(Calendar.MONTH)
        val lastMonthYear = timeHelper.cal.get(Calendar.YEAR)

        // Return all the dates whose month matches last month
        return expenses.filter {
            val expenseDate = timeHelper.sdf.parse(it.date)
            timeHelper.cal.time = expenseDate!!
            val expenseMonth = timeHelper.cal.get(Calendar.MONTH)
            val expenseYear = timeHelper.cal.get(Calendar.YEAR)

            expenseMonth == lastMonth && expenseYear == lastMonthYear
        }.toMutableList()
    }

    /**
     * Returns all expenses from the last 6 months
     */
    fun getExpensesLastSixMonths(): MutableList<Expense> {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Get all expenses
        val expenses = expenseDao.getAllExpenses()

        // Set curr dates
        val currDateStr = timeHelper.getCurrentDateTimeStr()

        // Find all expenses whose Year and Month matches last 6 months
        val currDate = timeHelper.sdf.parse(currDateStr)

        timeHelper.cal.time = currDate!!
        timeHelper.cal.add(Calendar.MONTH, -6)
        val sixMonthsAgoDate = timeHelper.cal.time

        // Return all the dates whose month matches last 6 months month
        return expenses.filter {
            val expenseDate = timeHelper.sdf.parse(it.date)

            !expenseDate!!.before(sixMonthsAgoDate) && !expenseDate.after(currDate)
        }.toMutableList()
    }

    /**
     * Returns all expenses for the year
     */
    fun getExpensesThisYear(): MutableList<Expense> {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Get all expenses
        val expenses = expenseDao.getAllExpenses()

        // Set curr dates
        val currDateStr = timeHelper.getCurrentDateTimeStr()
        val currDate = timeHelper.sdf.parse(currDateStr)
        timeHelper.cal.time = currDate!!

        // Find all expenses whose Year and Month matches this year
        val currYear = timeHelper.cal.get(Calendar.YEAR)

        // Return all the dates whose month matches this month
        return expenses.filter {
            val expenseDate = timeHelper.sdf.parse(it.date)
            timeHelper.cal.time = expenseDate!!
            val expenseYear = timeHelper.cal.get(Calendar.YEAR)

            expenseYear == currYear
        }.toMutableList()
    }

    /**
     * Inserts a new expense into the database
     */
    fun addExpense(expense: Expense) {
        // TimeHelper
        val timeHelper = TimeHelper()

        // Format date
        val expenseDate = timeHelper.sdf.parse(expense.date) as Date
        timeHelper.cal.time = expenseDate
        expense.date = timeHelper.sdf.format(timeHelper.cal.time)

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
        // TimeHelper
        val timeHelper = TimeHelper()

        // Format date
        val expenseDate = timeHelper.sdf.parse(expense.date) as Date
        timeHelper.cal.time = expenseDate
        expense.date = timeHelper.sdf.format(timeHelper.cal.time)

        return expenseDao.update(expense)
    }
}