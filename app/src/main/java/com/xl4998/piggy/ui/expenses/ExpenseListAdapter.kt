package com.xl4998.piggy.ui.expenses

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.data.db.entities.Subscription
import com.xl4998.piggy.ui.subscriptions.SubscriptionListAdapter
import com.xl4998.piggy.ui.subscriptions.SubscriptionUpdateDialogFragment
import org.w3c.dom.Text

/**
 * RecyclerView adapter for items in the ExpensesFragment's RecyclerView
 */
class ExpenseListAdapter(
    private val parentFragmentManager: FragmentManager,
    private val viewModel: ExpensesViewModel,
    private var expenseList: MutableList<Expense>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Reference to each item to be generated in RecyclerView
    internal class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.expense_category)
        val name: TextView = view.findViewById(R.id.expense_name)
        val cost: TextView = view.findViewById(R.id.expense_cost)
        val date: TextView = view.findViewById(R.id.expense_date)
        val removeBtn: Button = view.findViewById(R.id.expense_remove)
        val editBtn: Button = view.findViewById(R.id.expense_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Prepare card to be returned on RecyclerView creation
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_detail, parent, false) as MaterialCardView

        return DetailViewHolder(card)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Setup expense detail view
        val expense = expenseList[position]
        val viewHolder = holder as DetailViewHolder
        viewHolder.name.text = expense.name
        viewHolder.category.text = expense.category
        viewHolder.cost.text = "%.2f".format(expense.cost)
        viewHolder.date.text = "Transaction Date: %s".format(expense.date)

        // Prepare buttons listener
        viewHolder.removeBtn.setOnClickListener {
            viewModel.removeExpense(expense)
        }

        viewHolder.editBtn.setOnClickListener {
            // Show dialog fragment
            val dialog = ExpenseUpdateDialogFragment(viewModel)

            val bundle = Bundle()
            bundle.putLong("id", expense.id)
            bundle.putString("category", expense.category)
            bundle.putString("name", expense.name)
            bundle.putString("cost", expense.cost.toString())
            bundle.putString("date", expense.date)
            bundle.putString("desc", expense.desc)

            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "Update Expense Dialog")
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    /**
     * Sets the list of expense data to be populated in RecyclerView
     */
    fun setExpenses(expenseList: MutableList<Expense>) {
        this.expenseList = expenseList
        notifyDataSetChanged()
    }
}