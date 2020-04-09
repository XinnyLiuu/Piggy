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
import java.util.*

/**
 * Fragment that prepares the dashboard view to show the user's expenses
 */
class DashboardFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: DashboardViewModel

    // Pie Chart
    private lateinit var pie: PieChart

    // Dummy data
    private val parties: Array<String> = arrayOf(
        "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Repository
        // ViewModel
        viewModel = DashboardViewModel()

        // TODO: Setup observers
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
        pie.setExtraOffsets(25f, 10f, 25f, 5f)
        pie.setUsePercentValues(true)
        pie.rotationAngle = 0f
        pie.isRotationEnabled = true
        pie.isHighlightPerTapEnabled = true
        pie.animateY(2000, Easing.EaseInOutQuad)

        // Pie Chart center hole
        pie.isDrawHoleEnabled = true
        pie.holeRadius = 50f
        pie.transparentCircleRadius = 52f
        pie.setTransparentCircleColor(Color.WHITE)
        pie.setTransparentCircleAlpha(110)
        pie.setDrawCenterText(true)
        pie.setHoleColor(Color.WHITE)
        pie.centerText = generateCenterSpannableText()

        setData(5, 5f)

        // Pie Chart legend
        val legend: Legend = pie.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(true)
        legend.isEnabled = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Sets the data for the pie chart
     */
    private fun setData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range).toFloat() + range / 5,
                    parties[i % parties.size]
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        pie.data = data

        // undo all highlights
        pie.highlightValues(null)
        pie.invalidate()
    }

    /**
     * Create center text of pie chart
     */
    private fun generateCenterSpannableText(): SpannableString? {
        val header = "Expenses"
        val subHeader = "Piggy"

        val s = SpannableString("$header\n$subHeader")
        s.setSpan(RelativeSizeSpan(1.2f), 0, header.length, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), header.length, s.length, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), header.length, s.length, 0)
        return s
    }
}
