package com.xl4998.piggy.ui.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.utils.TimeFilters
import kotlinx.android.synthetic.main.fragment_expenses.*


/**
 * Fragment that displays the lists of all the user's expenses
 */
class ExpensesFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: ExpensesViewModel

    // RecyclerView
    private lateinit var rvAdapter: ExpenseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository
        val expenseRepository = ExpenseRepository(activity!!.application)

        // ViewModel
        viewModel = ExpensesViewModel(expenseRepository)

        // RecyclerView adapter
        rvAdapter = ExpenseListAdapter(parentFragmentManager, viewModel, mutableListOf())

        // Setup observers
        viewModel.liveAllExpenses.observe(this, Observer { expenses ->
            rvAdapter.setExpenses(expenses)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expenses, container, false)

        // Prepare RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.expense_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = rvAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show this month's expenses
        viewModel.getExpensesThisMonth()

        // Prepare dialog fragment
        val createExpenseDialog = ExpenseCreateDialogFragment(viewModel)

        // Setup listeners for button
        add_expense_button.setOnClickListener {
            createExpenseDialog.show(parentFragmentManager, "Create Expense Dialog")
            viewModel.getExpensesThisMonth()
        }

        // Setup time selection dropdown
        val times = listOf(
            TimeFilters.THIS_MONTH,
            TimeFilters.LAST_MONTH,
            TimeFilters.PAST_SIX_MONTHS,
            TimeFilters.THIS_YEAR,
            TimeFilters.ALL
        )

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, times)

        expense_time_filter.setAdapter(adapter)
        expense_time_filter.inputType = 0 // Disable input from time filter dropdown
        expense_time_filter.setText(expense_time_filter.adapter.getItem(0).toString(), false)

        // Set time filter dropdown listeners
        expense_time_filter.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                when(adapter.getItem(position)) {
                    // Show this month's expenses
                    TimeFilters.THIS_MONTH -> {
                        viewModel.getExpensesThisMonth()
                    }

                    // Show last month's expenses
                    TimeFilters.LAST_MONTH -> {
                        viewModel.getExpensesLastMonth()
                    }

                    // Show past six month's expenses
                    TimeFilters.PAST_SIX_MONTHS -> {
                        viewModel.getExpensesLastSixMonths()
                    }

                    // Show this year's expenses
                    TimeFilters.THIS_YEAR -> {
                        viewModel.getExpensesThisYear()
                    }

                    // Show all expenses
                    TimeFilters.ALL -> {
                        viewModel.getAllExpenses()
                    }
                }
            }
    }
}
