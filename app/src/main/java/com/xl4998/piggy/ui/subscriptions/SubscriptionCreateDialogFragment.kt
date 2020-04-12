package com.xl4998.piggy.ui.subscriptions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.entities.Subscription
import com.xl4998.piggy.utils.MaterialDatePickerDialog
import kotlinx.android.synthetic.main.fragment_subscription_create_dialog.*

/**
 * A full screen dialog that will assist in creating a new subscription
 *
 * Refer to https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
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
        val view: View =
            inflater.inflate(R.layout.fragment_subscription_create_dialog, container, false)

        // Get the toolbar
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Grab text fields
        val nameField = view.findViewById<TextView>(R.id.sub_name_field)
        val costField = view.findViewById<TextView>(R.id.sub_cost_field)
        val dateField = view.findViewById<TextView>(R.id.sub_date_field)
        val intervalField = view.findViewById<TextView>(R.id.sub_interval_field)

        // Setup listeners for the toolbar
        toolbar.title = "Add a Subscription"
        toolbar.inflateMenu(R.menu.fragment_subscription_create_menu)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.save_sub -> {
                    // Check text length
                    val name = nameField.text.toString().trim()
                    val cost = costField.text.toString().trim()
                    var date = dateField.text.toString().trim()
                    val interval = intervalField.text.toString().trim()

                    // Fix date from MM/dd/yyyy to yyyy-MM-dd
                    val times = date.split("/")
                    date = "%s-%s-%s".format(times[2], times[0], times[1])

                    if (name.isEmpty() || cost.isEmpty() || date.isEmpty() || interval.isEmpty()) {
                        Toast.makeText(context, "Please complete all fields!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // Create subscription
                        val sub = Subscription(
                            name.capitalize(),
                            "%.2f".format(cost.toDouble()).toDouble(),
                            date,
                            interval.toInt()
                        )

                        // Update subscription list view
                        viewModel.addNewSub(sub)

                        // Clear all texts
                        nameField.text = ""
                        costField.text = ""
                        dateField.text = ""
                        intervalField.text = ""

                        dismiss()
                    }
                }
                else -> {
                    dismiss()
                }
            }

            true
        }

        // Setup date picker dialog
        sub_date_field.setOnClickListener {
            MaterialDatePickerDialog(activity!!, it as TextView)
        }
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
