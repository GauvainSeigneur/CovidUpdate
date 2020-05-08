package com.gauvain.seigneur.covidupdate.widget.countryActiveHistoryChart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.CountryHistoryData
import com.gauvain.seigneur.covidupdate.widget.DayAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.view_all_history_chart.view.*

class CountryActiveHistoryChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val CRITICAL_TYPE = 1
    }

    private val x: XAxis

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_country_active_history_chart, this)
        this.orientation = VERTICAL
        initChart()
        x = chart.xAxis
    }

    private fun initChart() {
        // no description text
        chart.description.isEnabled = false
        // enable touch gestures
        chart.setTouchEnabled(true)
        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(false)
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.resetViewPortOffsets()

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f
        val y: YAxis = chart.axisLeft
        y.setDrawGridLines(false)
        val x: XAxis = chart.xAxis
        x.isEnabled = false // set to true if you want to see it again
        x.position = XAxisPosition.BOTTOM
        x.axisLineColor = ContextCompat.getColor(context, android.R.color.transparent)
        x.setDrawGridLines(false)
        x.labelCount = 5
        x.typeface = ResourcesCompat.getFont(context, R.font.work_sans_bold)
        x.valueFormatter = DayAxisValueFormatter()
        x.textColor = ContextCompat.getColor(context, R.color.colorWhite)
        x.removeAllLimitLines()

        chart.setViewPortOffsets(0f, 16f, 0f, 32f /*x.textSize + x.xOffset*/)

        chart.axisRight.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.legend.isEnabled = true
        chart.legend.textColor = ContextCompat.getColor(context, R.color.colorWhiteDarker)
        //chart.animateXY(500, 500)
        chart.invalidate()
    }

    fun setData(value: CountryHistoryData, label: String) {
        val activesEntries = value.activeChart.map {
            Entry(
                it.position,
                it.value
            )
        }
        val criticalEntries = value.criticalChart.map {
            Entry(
                it.position,
                it.value
            )
        }
        // create marker to display box when values are selected
        val mv = CountryActiveHistoryMarkerView(
            context,
            R.layout
                .view_country_active_history_marker,
            activesEntries,
            R.color.colorSecondary,
            criticalEntries,
            R.color.colorOrangeSplitSecondary
        )
        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv
        val dataSets = mutableListOf<ILineDataSet>()
        val activeSet = LineDataSet(activesEntries, label)
        val criticalSet = LineDataSet(criticalEntries, "critical")
        dataSets.add(activeSet)
        dataSets.add(criticalSet)
        //custom it
        customSet(activeSet, -1)
        customSet(criticalSet,
            CRITICAL_TYPE
        )
        // create a data object with the data sets
        val data = LineData(dataSets)
        //data.setValueTypeface(tfLight)
        data.setValueTextSize(30f)
        data.setDrawValues(false)
        // set data
        chart.data = data

        chart.animateXY(500, 500)
    }

    private fun customSet(set: LineDataSet, type: Int) {
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.05f
        set.setDrawFilled(true)
        set.lineWidth = 2.5f
        set.setDrawCircles(false)
        set.highlightLineWidth
        set.setDrawHorizontalHighlightIndicator(false)
        customColor(set, type)
    }

    private fun customColor(set: LineDataSet, type: Int) {
        when (type) {
            CRITICAL_TYPE -> {
                set.isHighlightEnabled = false // disable marker for this line.
                set.color = ContextCompat.getColor(context, R.color.colorOrangeSplitSecondary)
                set.fillDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.grandient_orange_chart
                )
            }
            else -> {
                set.highLightColor = ContextCompat.getColor(context, R.color.colorMarkerDark)
                //color of the line below marker
                set.color = ContextCompat.getColor(context, R.color.colorSecondaryLineChart)
                set.fillDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.gradient_secondary_chart
                )
            }
        }
    }
}