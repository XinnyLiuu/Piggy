package com.xl4998.piggy.ui.expenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.utils.ExpenseCategory
import kotlinx.android.synthetic.main.fragment_expense_create_dialog.*
import kotlinx.android.synthetic.main.fragment_subscription_create_dialog.*
import java.util.*

/**
 * A full screen dialog that will assist in creating a new expense
 *
 * Refer to https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
 */
class ExpenseCreateDialogFragment(
    private val viewModel: ExpensesViewModel
) : DialogFragment() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_expense_create_dialog, container, false)

        // Get the toolbar
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup category dropdown
        val categories = listOf(ExpenseCategory.ENTERTAINMENT, ExpenseCategory.FEES, ExpenseCategory.FOOD, ExpenseCategory.MISC, ExpenseCategory.PERSONAL, ExpenseCategory.SHOPPING, ExpenseCategory.SUBSCRIPTION, ExpenseCategory.TRANSPORTATION)
        val adapter = ArrayAdapter(requireContext(), R.layout.expense_category_item, categories)
        expense_category_field.setAdapter(adapter)

        // Setup listeners for the toolbar
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Add an Expense"
        toolbar.inflateMenu(R.menu.fragment_expense_create_menu)
        toolbar.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.save_expense -> {

                }
            }

            dismiss()
            true
        }

        // Setup date picker dialog
        expense_date_field.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            val y = cal.get(Calendar.YEAR)

            val picker = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    expense_date_field.setText(String.format("%s/%s/%s", month + 1, dayOfMonth, year))
                },
                y, m, d
            )

            picker.show()
        }

        // TODO: https://stackoverflow.com/questions/14036674/how-to-limit-the-text-in-numbers-only-from-0-59-in-edit-text-in-android
    }

    override fun onStart() {
        super.onStart()

        // Make the dialog appear fullscreen
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}
