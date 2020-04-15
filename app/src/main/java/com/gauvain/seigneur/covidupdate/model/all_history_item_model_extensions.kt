package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.AllHistoryItemModel
import com.gauvain.seigneur.domain.model.AllHistoryModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo

fun AllHistoryModel.toData(numberFormatProvider: NumberFormatProvider) =
    AllHistoryData(
        totalCases = numberFormatProvider.format(this.totalCases),
        activeCases = QuantityStringPresenter(
            R.plurals.main_header_chart_subtitle_active,
            this.totalActiveCases,
            numberFormatProvider.format(this.totalActiveCases)
        ),
        newCases = getNewCases(this, numberFormatProvider),
        history = setUpAllHistoryList(this.history)
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

private fun setUpAllHistoryList(list: List<AllHistoryItemModel>): List<AllHistoryItemData> {
    val dataList = mutableListOf<AllHistoryItemData>()
    for ((index, value) in list.reversed().withIndex()) {
        dataList.add(
            value.toData(value.day.time.toFloat())
        )
    }

    return dataList
}

private fun AllHistoryItemModel.toData(position: Float) =
    AllHistoryItemData(
        total = this.total.toFloat(),
        day = this.day.formatTo(DATA_DATE_FORMAT),
        position = position
    )
