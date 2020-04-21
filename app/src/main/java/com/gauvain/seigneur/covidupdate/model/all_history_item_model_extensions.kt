package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.AllHistoryItemModel
import com.gauvain.seigneur.domain.model.AllHistoryModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo
import com.github.mikephil.charting.data.Entry

fun AllHistoryModel.toData(numberFormatProvider: NumberFormatProvider) =
    AllHistoryData(
        totalCases = numberFormatProvider.format(this.totalCases),
        activeCases = QuantityStringPresenter(
            R.plurals.main_header_chart_subtitle_active,
            this.totalActiveCases,
            numberFormatProvider.format(this.totalActiveCases)
        ),
        newCases = getNewCases(this, numberFormatProvider),
        history = this.history.map { it.toData() },
        chart = setUpChartEntries(this.history)
    )

private fun getNewCases(
    businessModel: AllHistoryModel,
    numberFormatProvider: NumberFormatProvider
): AllHistoryNewCaseData =
    businessModel.totalNewCases?.let {
        AllHistoryNewCaseData(
            StringPresenter(
                R.string.main_header_chart_subtitle_new,
                numberFormatProvider.format(it)
            ),
            R.color.colorDanger
        )
    } ?: AllHistoryNewCaseData(
        StringPresenter(
            R.string.main_header_chart_subtitle_no_new_cases
        ),
        R.color.colorCool
    )

private fun setUpChartEntries(list: List<AllHistoryItemModel>): List<ChartAllHistoryItem> {
    val entryList = mutableListOf<ChartAllHistoryItem>()
    for ((index, value) in list.reversed().withIndex()) {
        entryList.add(
            ChartAllHistoryItem(
                value.day.time.toFloat(),
                value.total.toFloat()
            )
        )
    }

    return entryList
}

private fun AllHistoryItemModel.toData() =
    AllHistoryItemData(
        total = this.total,
        day = this.day.formatTo(DATA_DATE_FORMAT)
    )
