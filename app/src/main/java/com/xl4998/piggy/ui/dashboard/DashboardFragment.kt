package com.xl4998.piggy.ui.dashboard

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.ExpenseRepository
import com.xl4998.piggy.data.db.entities.Expense
import com.xl4998.piggy.utils.constants.TimeFilters
import kotlinx.android.synthetic.main.fragment_expenses.*

/**
 * Fragment that prepares the dashboard view to show the user's expenses
 */
class DashboardFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: DashboardViewModel

    // Pie Chart
    private lateinit var pie: PieChart

    // Colors for pie chart
    private val piggyColors = mutableListOf(
        ColorTemplate.rgb("#74d6e0"),
        ColorTemplate.rgb("#ecaaae"),
        ColorTemplate.rgb("#7fe1cf"),
        ColorTemplate.rgb("#e1b0dd"),
        ColorTemplate.rgb("#aad9a3"),
        ColorTemplate.rgb("#74aff3"),
        ColorTemplate.rgb("#d9da9e"),
        ColorTemplate.rgb("#bcb8ec"),
        ColorTemplate.rgb("#efb08d"),
        ColorTemplate.rgb("#a2bfe9"),
        ColorTemplate.rgb("#e3c297"),
        ColorTemplate.rgb("#7bcaed"),
        ColorTemplate.rgb("#e9bfae"),
        ColorTemplate.rgb("#dfc3de"),
        ColorTemplate.rgb("#b9d9be")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository
        val expenseRepository = ExpenseRepository(activity!!.application)

        // ViewModel
        viewModel = DashboardViewModel(expenseRepository)

        // Setup observers
        viewModel.liveAllExpenses.observe(this, Observer { expenses ->
            setData(expenses)
            pie.notifyDataSetChanged()
            pie.invalidate()
            pie.animateY(2000, Easing.EaseInOutQuad)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Setup Pie Chart
        pie = view.findViewById(R.id.pie)
        pie.description.isEnabled = false
        pie.dragDecelerationFrictionCoef = 0.95f
        pie.setExtraOffsets(15f, 10f, 15f, 10f)
        pie.setUsePercentValues(true)
        pie.rotationAngle = 0f
        pie.isRotationEnabled = true
        pie.isHighlightPerTapEnabled = true
        pie.animateY(2000, Easing.EaseInOutQuad)

        // Pie Chart center hole
        pie.isDrawHoleEnabled = true
        pie.holeRadius = 45f
        pie.transparentCircleRadius = 47f
        pie.setTransparentCircleColor(Color.WHITE)
        pie.setTransparentCircleAlpha(110)
        pie.setDrawCenterText(true)
        pie.setHoleColor(Color.WHITE)
        pie.centerText = generateCenterSpannableText()

        // Pie Chart legend
        val legend: Legend = pie.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.isEnabled = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show this month's expenses
        viewModel.getExpensesThisMonth()

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
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (adapter.getItem(position)) {
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

    /**
     * Sets the data for the pie chart
     */
    private fun setData(expenses: MutableList<Expense>) {
        piggyColors.shuffle()

        // Entries to be represented in the pie chart
        val entries = mutableListOf<PieEntry>()

        // Add each expense by category in the pie chart
        val costPerCategory = hashMapOf<String, Double?>()

        for (e in expenses) {
            val category = e.category
            val cost = e.cost

            if (category in costPerCategory) {
                costPerCategory[category] = costPerCategory[category]?.plus(cost)
            } else costPerCategory[category] = e.cost
        }

        for ((category, cost) in costPerCategory) {
            val label: String = "%.2f".format(cost)

            entries.add(
                PieEntry(cost!!.toFloat(), "$category - $label")
            )
        }

        // Data Set configs
        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = piggyColors
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        // Data setup
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        pie.data = data
    }

    /**
     * Create center text of pie chart
     */
    private fun generateCenterSpannableText(): SpannableString? {
        val header = "Expenses"
        val subHeader = "Powered by Piggy"

        val s = SpannableString("$header\n$subHeader")
        s.setSpan(RelativeSizeSpan(1.2f), 0, header.length, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), header.length, s.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), header.length, s.length, 0)
        return s
    }
}
