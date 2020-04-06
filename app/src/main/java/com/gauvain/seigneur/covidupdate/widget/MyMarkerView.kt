package com.gauvain.seigneur.covidupdate.widget

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.gauvain.seigneur.covidupdate.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    private val tvContent: TextView
    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    fun refreshContent(e: Map.Entry<*, *>, highlight: Highlight?) {
        if (e is CandleEntry) {
            val ce = e as CandleEntry
            tvContent.text = Utils.formatNumber(ce.high, 0, true)
        } else {
            tvContent.text = Utils.formatNumber((e as CandleEntry).y, 0, true)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        tvContent = findViewById(R.id.totalCasesTextView)
    }
}