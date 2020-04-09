package com.xl4998.piggy.ui.subscriptions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.fragment_subscription_create_dialog.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

/**
 * A full screen dialog that will assist in updating an existing subscription
 *
 * Refer to https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
 */
class SubscriptionUpdateDialogFragment(
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
            inflater.inflate(R.layout.fragment_subscription_update_dialog, container, false)

        // Get the toolbar
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fill text fields with subscription details
        val nameField = view.findViewById<TextView>(R.id.sub_name_field)
        nameField.text = arguments!!.getString("name")
        val costField = view.findViewById<TextView>(R.id.sub_cost_field)
        costField.text = arguments!!.getString("cost")
        val dateField = view.findViewById<TextView>(R.id.sub_date_field)
        dateField.text = arguments!!.getString("date")
        val intervalField = view.findViewById<TextView>(R.id.sub_interval_field)
        intervalField.text = arguments!!.getString("interval")

        val oldName: String = arguments!!.getString("name")!!

        // Setup listeners for the toolbar
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "Edit Subscription"
        toolbar.inflateMenu(R.menu.fragment_subscription_create_menu)
        toolbar.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.save_sub -> {
                    // Check text length
                    val name = nameField.text.toString().trim()
                    val cost = costField.text.toString().trim()
                    val date = dateField.text.toString().trim()
                    val interval = intervalField.text.toString().trim()

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
                        viewModel.updateSub(sub, oldName)

                        // Clear all texts
                        nameField.text = ""
                        costField.text = ""
                        dateField.text = ""
                        intervalField.text = ""
                    }
                }
            }

            dismiss()
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
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
}
