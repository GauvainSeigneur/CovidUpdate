package com.gauvain.seigneur.covidupdate.view.main

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.widget.MyMarkerView
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.view_header_stat.view.*

class HeaderStatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_header_stat, this)
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
        totalPieChart.legend.isEnabled =false
    }

    fun setData() {
        val entries: ArrayList<PieEntry> = ArrayList()
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(PieEntry(0.5f, "us"))
        entries.add(PieEntry(0.2f, "fr"))
        entries.add(PieEntry(0.15f, "be"))
        entries.add(PieEntry(0.10f, "uk"))
        entries.add(PieEntry(0.05f, "ja"))


        val dataSet = PieDataSet(entries, "Election Results")
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
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(totalPieChart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        totalPieChart.setData(data)
        // undo all highlights
        totalPieChart.highlightValues(null)
        totalPieChart.invalidate()
    }
}