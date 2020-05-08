package com.gauvain.seigneur.covidupdate.widget.countryActiveHistoryChart

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.view_country_active_history_marker.view.*
import java.util.*

@SuppressLint("ViewConstructor")
class CountryActiveHistoryMarkerView(
    context: Context?,
    layoutResource: Int,
    private val activeEntries: List<Entry>,
    @ColorRes
    private val activeColor: Int,
    private val criticalEntries: List<Entry>,
    @ColorRes
    private val criticalColor: Int
) :
    MarkerView(context, layoutResource) {

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        val xFormatted = Date(e.x.toLong()).formatTo(DATA_DATE_FORMAT)
        xValueTextView.text = xFormatted
        val activeValue = (activeEntries.firstOrNull { it.x == e.x }?.y) ?: 0f
        val criticalValue = (criticalEntries.firstOrNull { it.x == e.x }?.y) ?: 0f

        activeCasesTextView.text = Utils.formatNumber(activeValue, 0, true)
        setIcon(activeCasesTextView, activeColor)
        criticalCasesTextView.text = Utils.formatNumber(criticalValue, 0, true)
        setIcon(criticalCasesTextView, criticalColor)
        super.refreshContent(e, highlight)
    }


    private fun setIcon(
        textView: TextView,
        @ColorRes
        color: Int
    ) {
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_marker_color_drawable)
        textView.setCompoundDrawablesWithIntrinsicBounds(
            icon,
            null,
            null,
            null
        )

        for (d in textView.compoundDrawables) {
            d?.let {
                DrawableCompat.setTint(DrawableCompat.wrap(it).mutate(), getColor(context, color))
            }
        }
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}