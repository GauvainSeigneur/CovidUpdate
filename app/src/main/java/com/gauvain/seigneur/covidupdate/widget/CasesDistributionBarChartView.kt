package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.CaseStateDistributionItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.StackedValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.view_cases_distribution_bar_chart.view.*

class CasesDistributionBarChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val x: XAxis

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_cases_distribution_bar_chart, this)
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
        //show or not the grid background
        chart.setDrawGridBackground(false)
        //Manage y and x axis
        val y: YAxis = chart.axisLeft
        y.setDrawGridLines(false)
        val x: XAxis = chart.xAxis
        x.isEnabled = false // set to true if you want to see it again*/
        x.position = XAxis.XAxisPosition.BOTTOM
        x.axisLineColor = ContextCompat.getColor(context, android.R.color.transparent)
        x.setDrawGridLines(false)
        //x.labelCount = 5
        x.typeface = ResourcesCompat.getFont(context, R.font.work_sans_bold)
        x.valueFormatter = DayAxisValueFormatter()
        x.textColor = ContextCompat.getColor(context, R.color.colorWhite)
        x.removeAllLimitLines()
        //viewport
        //chart.setViewPortOffsets(0f, 16f, 0f, 0f /*x.textSize + x.xOffset*/)
        //show axis
        chart.axisRight.isEnabled = false
        chart.axisLeft.isEnabled = false
        //legend
        chart.legend.isEnabled = true
        chart.legend.textColor = ContextCompat.getColor(context, R.color.colorWhiteDarker)
        //Bar chart specific customization
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(false)
        chart.setHighlightFullBarEnabled(false)
        // create marker to display box when values are selected
        val mv = MyMarkerView(context, R.layout.view_marker)
        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv
        chart.invalidate()
    }

    fun setData(values: List<CaseStateDistributionItem>, label: String) {
        val entries = values.map {
            BarEntry(
                it.position,
                floatArrayOf(it.nonCritical, it.critical, it.dead, it.recovered)
            )
        }
        val set = BarDataSet(entries, label)
        set.highLightColor = ContextCompat.getColor(context, R.color.colorSecondary)
        set.setDrawIcons(false)
        set.setColors(
            ContextCompat.getColor(context, R.color.colorSecondary),
            ContextCompat.getColor(context, R.color.colorOrangeSplitSecondary),
            ContextCompat.getColor(context, R.color.colorDanger),
            ContextCompat.getColor(context, R.color.colorCool)
        )
        set.setStackLabels(arrayOf("Births", "Divorces", "Marriages"))
        // create a data object with the data sets
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set)
        val data = BarData(dataSets)
        data.barWidth = 0.3F
        //data.setValueTypeface(tfLight)
        data.setValueTextSize(30f)
        data.setDrawValues(false)
        data.setValueFormatter(StackedValueFormatter(false, "", 1))
        // set data
        //setXAxisMinMax(entries)
        chart.data = data
        chart.setFitBars(true)
        chart.animateXY(500, 500)
        chart.invalidate()
    }
}