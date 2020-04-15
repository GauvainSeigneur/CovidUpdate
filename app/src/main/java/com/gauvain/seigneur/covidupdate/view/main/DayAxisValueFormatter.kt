package com.gauvain.seigneur.covidupdate.view.main

import com.gauvain.seigneur.domain.utils.CHART_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo

import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class DayAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val time = value.toLong()
        val netDate = Date(time)
        return netDate.formatTo(CHART_DATE_FORMAT)

    }
}