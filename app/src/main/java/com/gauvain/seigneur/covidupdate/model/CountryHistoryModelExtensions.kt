package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.domain.model.CountryHistoryItemModel
import com.gauvain.seigneur.domain.model.CountryHistoryModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo
import kotlin.math.ceil

private const val ACTIVE_TYPE = 0
private const val CRITICAL_TYPE = 1
private const val DISTRIBUTION_CASE_COL_NUMBER = 7.0f

fun CountryHistoryModel.toData(numberFormatProvider: NumberFormatProvider) =
    CountryHistoryData(
        casesList = setUpCasesList(this.history, numberFormatProvider),
        activeChart = setUpChartEntries(this.history, ACTIVE_TYPE),
        criticalChart = setUpChartEntries(this.history, CRITICAL_TYPE),
        caseDistributionChart = setUpDistributionChart(this.history)
    )

private fun setUpCasesList(list: List<CountryHistoryItemModel>,
                           numberFormatProvider: NumberFormatProvider): List<CountryCasesData> {
    val item = list[0]
    return listOf(
        CountryCasesData(
           numberFormatProvider.format(item.active - item.critical),
            R.color.colorCaseActive
        ),
        CountryCasesData(
            numberFormatProvider.format(item.critical),
            R.color.colorCaseCritical
        ),
        CountryCasesData(
            numberFormatProvider.format(item.recovered),
            R.color.colorCaseRecovered
        ),
        CountryCasesData(
            numberFormatProvider.format(item.dead),
            R.color.colorCaseDead
        )
    )
}

private fun setUpDistributionChart(list: List<CountryHistoryItemModel>):
    List<CaseStateDistributionItem> {
    var index = 0
    val distributedCasesByWeek = mutableListOf<CaseStateDistributionItem>()
    list.reversed().chunked(getChunkedList(list)) {
        distributedCasesByWeek.add(CaseStateDistributionItem(
            startDate = it[0].date.formatTo(DATA_DATE_FORMAT),
            enDate = it[it.size - 1].date.formatTo(DATA_DATE_FORMAT),
            position = index.toFloat(),
            nonCritical = percent((
                it.sumBy { item -> item.active } - it.sumBy { item -> item.critical }),
                it.sumBy { item -> item.total }),
            critical = percent(
                it.sumBy { item -> item.critical },
                it.sumBy { item -> item.total }),
            recovered = percent(
                it.sumBy { item -> item.recovered },
                it.sumBy { item -> item.total }),
            dead = percent(it.sumBy { item -> item.dead }, it.sumBy { item -> item.total }
            )))
        index += 1
    }
    return distributedCasesByWeek
}

private fun getChunkedList(list: List<CountryHistoryItemModel>): Int =
    ceil(list.size / DISTRIBUTION_CASE_COL_NUMBER).toInt()

private fun percent(n: Int, v: Int): Float = (n * 100.0f) / v

private fun setUpChartEntries(list: List<CountryHistoryItemModel>, type: Int):
    List<CountryChartHistoryItem> {
    val entryList = mutableListOf<CountryChartHistoryItem>()
    for ((index, value) in list.reversed().withIndex()) {
        entryList.add(
            CountryChartHistoryItem(
                value.date.time.toFloat(),
                when (type) {
                    ACTIVE_TYPE -> value.active.toFloat()
                    CRITICAL_TYPE -> value.critical.toFloat()
                    else -> 0f
                }
            )
        )
    }

    return entryList
}


