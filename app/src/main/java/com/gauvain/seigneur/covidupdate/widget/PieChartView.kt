package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.gauvain.seigneur.covidupdate.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.view_pie_chart.view.*

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_pie_chart, this)
        initPieChart()
    }

    private fun initPieChart() {
        totalPieChart.setUsePercentValues(true)
        totalPieChart.description.isEnabled =false
        //totalPieChart.setExtraOffsets(5f, 10f, 5f, 5f);

        // create marker to display box when values are selected
        //val mv = MyMarkerView(this, R.layout.custom_marker_view)
        // Set the marker to the chart
        //mv.setChartView(totalPieChart)
        //totalPieChart.setMarker(mv)

        //
        totalPieChart.dragDecelerationFrictionCoef =0.95f

        //totalPieChart.setCenterTextTypeface(tfLight);
        totalPieChart.centerText = "Total"

        //do not display data labels inside each part of the pie
        totalPieChart.setDrawEntryLabels(false)

        totalPieChart.isDrawHoleEnabled = true
        totalPieChart.setHoleColor(Color.TRANSPARENT)

        totalPieChart.setTransparentCircleColor(Color.WHITE)
        totalPieChart.setTransparentCircleAlpha(110)

        totalPieChart.holeRadius =35f
        totalPieChart.transparentCircleRadius = 38f

        totalPieChart.setDrawCenterText(true);

        totalPieChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        totalPieChart.isRotationEnabled =true
        totalPieChart.isHighlightPerTapEnabled = true
        //legend
        val l: Legend = totalPieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        //l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        l.textColor = ContextCompat.getColor(context, R.color.colorWhite)

        //make it half
        /*totalPieChart.setMaxAngle(180f)
        totalPieChart.setRotationAngle(180f);
        totalPieChart.setCenterTextOffset(0f, -20f)*/
    }

    fun setData(entries: ArrayList<PieEntry>, label:String) {
        val dataSet = PieDataSet(entries, label)
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(totalPieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        totalPieChart.setData(data)
        // undo all highlights
        totalPieChart.highlightValues(null)
        totalPieChart.animateY(1400, Easing.EaseInOutQuad)
        totalPieChart.invalidate()
    }
}