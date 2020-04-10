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
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.*

/**
 * Fragment that prepares the dashboard view to show the user's expenses
 */
class DashboardFragment : Fragment() {

    // Coroutine
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    // Repository
    private lateinit var expenseRepository: ExpenseRepository

    // Pie Chart
    private lateinit var pie: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository
        expenseRepository = ExpenseRepository(activity!!.application)
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
//        pie.setEntryLabelColor(Color.BLACK)

        // Pie Chart center hole
        pie.isDrawHoleEnabled = true
        pie.holeRadius = 45f
        pie.transparentCircleRadius = 47f
        pie.setTransparentCircleColor(Color.WHITE)
        pie.setTransparentCircleAlpha(110)
        pie.setDrawCenterText(true)
        pie.setHoleColor(Color.WHITE)
        pie.centerText = generateCenterSpannableText()

        setData()

        // Pie Chart legend
        val legend: Legend = pie.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.isEnabled = true

        return view
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    /**
     * Sets the data for the pie chart
     */
    private fun setData() {
        // Entries to be represented in the pie chart
        val entries = mutableListOf<PieEntry>()

        // Grab the latest expenses
        var expenses: List<Expense>? = null

        uiScope.launch {
            withContext(Dispatchers.IO) {
                expenses = expenseRepository.getAllExpenses()

                // Colors for pie chart
                val piggyColors = mutableListOf(
                    ColorTemplate.rgb("#5026a7"),
                    ColorTemplate.rgb("#8d448b"),
                    ColorTemplate.rgb("#cc6a87"),
                    ColorTemplate.rgb("#eccd8f"),
                    ColorTemplate.rgb("#ffbea3"),
                    ColorTemplate.rgb("#fe9191"),
                    ColorTemplate.rgb("#e4406f"),
                    ColorTemplate.rgb("#ca2374"),
                    ColorTemplate.rgb("#9c297f")
                )

                piggyColors.shuffle()

                // Add each expense by category in the pie chart
                val costPerCategory = hashMapOf<String, Double?>()

                for (e in expenses!!) {
                    val category = e.category
                    val cost = e.cost

                    if (category in costPerCategory) {
                        costPerCategory[category] = costPerCategory[category]?.plus(cost)
                    } else costPerCategory[category] = e.cost
                }

                for ((category, cost) in costPerCategory) {
                    entries.add(
                        PieEntry(cost!!.toFloat(), category)
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
        }
    }

    /**
     * Create center text of pie chart
     */
    private fun generateCenterSpannableText(): SpannableString? {
        val header = "Expenses"
        val subHeader = "Powered by Piggy" // TODO: Make this for the current month

        val s = SpannableString("$header\n$subHeader")
        s.setSpan(RelativeSizeSpan(1.2f), 0, header.length, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), header.length, s.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), header.length, s.length, 0)
        return s
    }
}
