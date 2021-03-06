package com.gauvain.seigneur.covidupdate.widget.activeHistoryChart

import android.annotation.SuppressLint
import android.content.Context
import com.gauvain.seigneur.presentation.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.presentation.utils.formatTo
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.view_marker.view.*
import java.util.*

@SuppressLint("ViewConstructor")
class ActiveHistoryMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        yValueTextView.text = Utils.formatNumber(e.y, 0, true)
        val xFormatted = Date(e.x.toLong()).formatTo(DATA_DATE_FORMAT)
        xValueTextView.text = xFormatted

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}