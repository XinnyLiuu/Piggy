package com.xl4998.piggy.ui.expenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.data.db.entities.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for Expenses fragments
 */
class ExpensesViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    // Data
    private var allExpenses: MutableList<Expense> = mutableListOf()
    var liveAllExpenses: MutableLiveData<MutableList<Expense>> = MutableLiveData()

    init {
        // Grab all Expenses
        getAllExpenses()
    }

    /**
     * Grabs all expenses
     */
    fun getAllExpenses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allExpenses = expenseRepository.getAllExpenses()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Filter for this month's expenses
     */
    fun getExpensesThisMonth() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Get this month's expenses
                allExpenses = expenseRepository.getExpensesThisMonth()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Filter for last month's expenses
     */
    fun getExpensesLastMonth() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Get last month's expenses
                allExpenses = expenseRepository.getExpensesLastMonth()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Filter for last six month's expenses
     */
    fun getExpensesLastSixMonths() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Get last six month's expenses
                allExpenses = expenseRepository.getExpensesLastSixMonths()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Filter for last year's expenses
     */
    fun getExpensesThisYear() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Get last year's expenses
                allExpenses = expenseRepository.getExpensesThisYear()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Inserts a new expense into the database
     */
    fun addNewExpense(expense: Expense) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Add the expense
                expenseRepository.addExpense(expense)
                allExpenses =
                    expenseRepository.getExpensesThisMonth() // Default to show this month's list in GUI
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Removes a stored expense
     */
    fun removeExpense(expense: Expense) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Remove the expense
                expenseRepository.removeExpense(expense)
                allExpenses =
                    expenseRepository.getExpensesThisMonth() // Default to show this month's list in GUI
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Update a stored expense
     */
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Update the expense
                expenseRepository.updateExpense(expense)
                allExpenses =
                    expenseRepository.getExpensesThisMonth() // Default to show this month's list in GUI
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }
}
