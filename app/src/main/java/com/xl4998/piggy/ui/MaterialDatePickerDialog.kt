package com.xl4998.piggy.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.widget.TextView
import com.xl4998.piggy.utils.TimeHelper
import java.util.*

/**
 * Custom class to show a DatePickerDialog
 */
class MaterialDatePickerDialog(
    activity: Activity,
    private val textView: TextView
) {
    private val timeHelper = TimeHelper()
    private val cal: Calendar = timeHelper.cal
    private val m = cal.get(Calendar.MONTH)
    private val d = cal.get(Calendar.DAY_OF_MONTH)
    private val y = cal.get(Calendar.YEAR)

    init {
        val picker = DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                textView.text = String.format("%s/%s/%s", month + 1, dayOfMonth, year)
            },
            y, m, d
        )

        picker.datePicker.maxDate = timeHelper.cal.timeInMillis
        picker.show()
    }
}