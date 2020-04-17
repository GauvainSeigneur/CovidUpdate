package com.gauvain.seigneur.covidupdate.widget

import com.gauvain.seigneur.domain.utils.CHART_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo

import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class DayAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return Date(value.toLong()).formatTo(CHART_DATE_FORMAT)

    }
}