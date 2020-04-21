package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.ChartAllHistoryItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.view_all_history_chart.view.*
import org.w3c.dom.EntityReference

class AllHistoryChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val x: XAxis

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_all_history_chart, this)
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
        x.isEnabled = true
        x.position = XAxisPosition.BOTTOM
        x.axisLineColor = ContextCompat.getColor(context, android.R.color.transparent)
        x.setDrawGridLines(false)
        x.labelCount = 5
        x.typeface = ResourcesCompat.getFont(context, R.font.work_sans_bold)
        x.valueFormatter = DayAxisValueFormatter()
        x.textColor = ContextCompat.getColor(context, R.color.colorWhite)
        x.removeAllLimitLines()

        chart.setViewPortOffsets(0f, 16f, 0f, x.textSize + x.xOffset)

        chart.axisRight.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.legend.isEnabled = false
        // create marker to display box when values are selected
        val mv = MyMarkerView(context, R.layout.view_marker)
        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv
        //chart.animateXY(500, 500)
        chart.invalidate()
    }

    fun setData(values: List<ChartAllHistoryItem>, label: String) {
        val entries =  values.map {
            Entry(
                it.position,
                it.value
            )
        }
        val set = LineDataSet(entries, label)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.25f
        set.setDrawFilled(true)
        set.lineWidth = 4.5f
        set.setDrawCircles(false)
        set.highlightLineWidth
        set.highLightColor = ContextCompat.getColor(context, R.color.colorSecondary)
        set.color = ContextCompat.getColor(context, R.color.colorSecondaryLineChart)
        set.fillDrawable = ContextCompat.getDrawable(context, R.drawable.gradient_header_chart)
        set.setDrawHorizontalHighlightIndicator(false)
        // create a data object with the data sets
        val data = LineData(set)
        //data.setValueTypeface(tfLight)
        data.setValueTextSize(30f)
        data.setDrawValues(false)
        // set data
        setXAxisMinMax(entries)
        chart.data = data
        chart.animateXY(500, 500)
    }

    private fun setXAxisMinMax(entries: List<Entry>) {
        val total = entries.size - 1
        if (total % 2 == 0) {
        } else {
            x.axisMinimum = entries[4].x
        }
        chart.invalidate()
    }
}