package com.xl4998.piggy.ui.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.ui.subscriptions.SubscriptionCreateDialogFragment
import com.xl4998.piggy.ui.subscriptions.SubscriptionListAdapter
import kotlinx.android.synthetic.main.fragment_expenses.*
import kotlinx.android.synthetic.main.fragment_subscriptions.*

/**
 * Fragment that displays the lists of all the user's expenses
 */
class ExpensesFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: ExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository
        val expenseRepository = ExpenseRepository(activity!!.application)

        // ViewModel
        viewModel = ExpensesViewModel(expenseRepository)

        // RecyclerView adapter TODO

        // Setup observers TODO
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
//        recyclerView.adapter = rvAdapter

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
