package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.presentation.model.CaseStateDistributionItem
import com.gauvain.seigneur.presentation.model.CountryHistoryData
import com.gauvain.seigneur.covidupdate.widget.casesDristributionChart.CasesDistributionBarChartView
import com.gauvain.seigneur.covidupdate.widget.criticalActiveHistoryChart.CriticalActiveHistoryChartView
import kotlinx.android.synthetic.main.view_details_stat_block.view.*

class DetailsStatBlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_details_stat_block, this)
        orientation = VERTICAL
        context.obtainStyledAttributes(
            attrs,
            R.styleable.DetailsStatBlockView,
            0,
            0
        ).apply {
            title.text = getString(R.styleable.DetailsStatBlockView_title)
            inflateChart(getResourceId(R.styleable.DetailsStatBlockView_chart, 0))
        }.recycle()
    }

    private fun inflateChart(chartLayoutId: Int?) {
        if (chartLayoutId != null && chartLayoutId != 0) {
            LayoutInflater.from(context).inflate(chartLayoutId, chartRootLayout, true)
        }
    }

    /**
     * Set data for CriticalActiveHistoryChartView
     */
    fun setData(value: CountryHistoryData, label: String?) {
        (chartRootLayout.getChildAt(0) as? CriticalActiveHistoryChartView)?.setData(value, label)
    }

    /**
     * Set data for CasesDistributionBarChartView
     */
    fun setData(value: List<CaseStateDistributionItem>, label: String?) {
        (chartRootLayout.getChildAt(0) as? CasesDistributionBarChartView)?.setData(value, label)
    }
}