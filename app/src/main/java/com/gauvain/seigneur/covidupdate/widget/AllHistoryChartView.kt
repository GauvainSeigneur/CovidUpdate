package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.gauvain.seigneur.covidupdate.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import kotlinx.android.synthetic.main.view_all_history_chart.view.*

class AllHistoryChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_all_history_chart, this)
        this.orientation = VERTICAL
        initChart()
    }

    private fun initChart() {
        allHistoryPieChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        //allHistoryPieChart.setBackgroundColor(Color.rgb(104, 241, 175))
        // no description text
        allHistoryPieChart.description.isEnabled = false
        // enable touch gestures
        allHistoryPieChart.setTouchEnabled(true)
        // enable scaling and dragging
        allHistoryPieChart.setDragEnabled(true)
        allHistoryPieChart.setScaleEnabled(true)
        // if disabled, scaling can be done on x- and y-axis separately
        allHistoryPieChart.setPinchZoom(false)

        allHistoryPieChart.setDrawGridBackground(false)
        /*allHistoryPieChart.setMaxHighlightDistance(300f)
        val x: XAxis = allHistoryPieChart.xAxis
        x.isEnabled = true
        x.position = XAxisPosition.BOTTOM
        val y: YAxis = allHistoryPieChart.axisLeft
        //y.typeface = tfLight
        y.setLabelCount(6, false)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE

        allHistoryPieChart.axisRight.isEnabled = false
        allHistoryPieChart.axisLeft.isEnabled = false

        allHistoryPieChart.legend.isEnabled = false*/

        allHistoryPieChart.animateXY(2000, 2000)
    }

    fun setData(entries: List<Entry>, label: String) {
        val set1 = LineDataSet(entries, label)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(Color.WHITE)
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.color = Color.WHITE
        set1.fillColor = ContextCompat.getColor(context, R.color.colorBackgroundLight)
        set1.fillAlpha = 50
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter = IFillFormatter { dataSet, dataProvider ->
            allHistoryPieChart.getAxisLeft().getAxisMinimum()
        }
        // create a data object with the data sets
        val data = LineData(set1)
        //data.setValueTypeface(tfLight)
        data.setValueTextSize(9f)
        data.setDrawValues(false)
        // set data
        allHistoryPieChart.setData(data)
        allHistoryPieChart.invalidate()
    }
}