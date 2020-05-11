package com.gauvain.seigneur.covidupdate.widget.casesDristributionChart

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.CaseStateDistributionItem
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.view_cases_distribution_marker.view.*
import java.text.DecimalFormat

@SuppressLint("ViewConstructor")
class CasesDistributionMarkerView(
    context: Context?,
    layoutResource: Int,
    private val cases: List<CaseStateDistributionItem>,
    private val colors: IntArray
) : MarkerView(context, layoutResource) {

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        val item = cases.firstOrNull { it.position == e.x }
        periodCasesTextView.text = String.format(
            context.getString(R.string.period_start_end),
            item?.startDate,
            item?.enDate
        )
        val formatter = DecimalFormat("00.##")
        nonCriticalCasesTextView.setData(
            String.format(context.getString(R.string.percentage_value),
                formatter.format(item?.nonCritical)), colors[0])

        criticalCasesTextView.setData(
            String.format(context.getString(R.string.percentage_value),
                formatter.format(item?.critical)), colors[1])

        deadCasesTextView.setData(
            String.format(context.getString(R.string.percentage_value),
                formatter.format(item?.dead)), colors[2])

        recoveredCasesTextView.setData(
            String.format(context.getString(R.string.percentage_value),
                formatter.format(item?.recovered)), colors[3])

        super.refreshContent(e, highlight)
    }

    private fun TextView.setData(
        value: String,
        color: Int
    ) {
        this.text = value
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_marker_color_drawable)
        this.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)

        for (d in this.compoundDrawables) {
            d?.let {
                DrawableCompat.setTint(DrawableCompat.wrap(it).mutate(), color)
            }
        }
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}