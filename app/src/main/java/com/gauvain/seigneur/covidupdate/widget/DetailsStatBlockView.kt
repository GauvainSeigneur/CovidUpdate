package com.gauvain.seigneur.covidupdate.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.TypedArrayUtils.getString
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.use
import kotlinx.android.synthetic.main.view_details_stat_block.view.*

class DetailsStatBlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var chartLayout: Int? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_details_stat_block, this)
        orientation = VERTICAL
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.DetailsStatBlockView,
            0,
            0
        )

        typedArray.use {
            title.text = getString(R.styleable.DetailsStatBlockView_title)
            chartLayout = getResourceId(R.styleable.DetailsStatBlockView_chart, 0)
        }
        inflateChart()
    }

    private fun inflateChart() {
        if (chartLayout != null && chartLayout != 0) {
            LayoutInflater.from(context).inflate(chartLayout!!, chartRootLayout, true)
        }
    }
}