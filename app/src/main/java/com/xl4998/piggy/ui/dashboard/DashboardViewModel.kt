package com.xl4998.piggy.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.data.db.entities.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for Dashboard fragments
 */
class DashboardViewModel(
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
                allExpenses = expenseRepository.getAllExpenses().toMutableList()
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
}