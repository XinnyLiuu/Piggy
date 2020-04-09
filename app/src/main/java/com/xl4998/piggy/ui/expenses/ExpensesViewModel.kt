package com.xl4998.piggy.ui.expenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.data.db.entities.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                allExpenses = expenseRepository.getAllExpenses().toMutableList()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }

    /**
     * Grab an expense by name
     */
    fun getExpenseById(id: Long): Expense? {
        var expense: Expense? = null

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                expense = expenseRepository.getExpenseById(id)
            }
        }

        return expense
    }

    /**
     * Inserts a new expense into the database
     */
    fun addNewExpense(expense: Expense) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Add the expense
                expenseRepository.addExpense(expense)
                allExpenses = expenseRepository.getAllExpenses().toMutableList()
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
                allExpenses = expenseRepository.getAllExpenses().toMutableList()
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
                allExpenses = expenseRepository.getAllExpenses().toMutableList()
                liveAllExpenses.postValue(allExpenses)
            }
        }
    }
}
