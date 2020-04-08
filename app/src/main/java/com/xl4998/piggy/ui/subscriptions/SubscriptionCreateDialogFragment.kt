package com.xl4998.piggy.ui.subscriptions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.SubscriptionRepository
import com.xl4998.piggy.data.db.entities.Subscription
import kotlinx.android.synthetic.main.fragment_subscription_create.*
import java.util.*

/**
 * A full screen dialog that will assist in creating a new subscription
 *
 * Code taken from https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
 */
class SubscriptionCreateDialogFragment(
    private val viewModel: SubscriptionsViewModel
) : DialogFragment() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_subscription_create, container, false)

        // Get the toolbar
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listeners for the toolbar
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Add a Subscription"
        toolbar.inflateMenu(R.menu.fragment_subscription_create_menu)
        toolbar.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.save_sub -> {
                    // Check text length
                    val name = view.findViewById<TextView>(R.id.sub_name_field).text.toString().trim()
                    val cost = view.findViewById<TextView>(R.id.sub_cost_field).text.toString().trim()
                    val date = view.findViewById<TextView>(R.id.sub_date_field).text.toString().trim()
                    val interval = view.findViewById<TextView>(R.id.sub_interval_field).text.toString().trim()

                    if (name.isEmpty() ||
                        cost.isEmpty() ||
                        date.isEmpty() ||
                        interval.isEmpty()) {
                        Toast.makeText(context, "Please complete all fields!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Create subscription
                        val sub = Subscription(name, cost.toDouble(), date, interval.toInt())

                        // Call parent fragment's ViewModel
                        viewModel.addNewSub(sub)

                        dismiss()
                    }
                }
                else -> dismiss()
            }

            true
        }

        // Setup date picker dialog
        sub_date_field.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            val y = cal.get(Calendar.YEAR)

            val picker = DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    sub_date_field.setText(String.format("%s/%s/%s", month + 1, dayOfMonth, year))
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
        if (dialog != null) dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
