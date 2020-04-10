package com.xl4998.piggy.ui.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.ExpenseRepository
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

        // Prepare dialog fragment
        val createExpenseDialog = ExpenseCreateDialogFragment(viewModel)

        // Setup listeners for button
        add_expense_button.setOnClickListener {
            createExpenseDialog.show(parentFragmentManager, "Create Expense Dialog")
        }
    }
}
