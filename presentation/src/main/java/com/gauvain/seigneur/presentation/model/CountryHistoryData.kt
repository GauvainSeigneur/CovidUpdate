package com.gauvain.seigneur.presentation.model

import androidx.annotation.ColorRes

data class CountryHistoryData(
    val casesList : List<CountryCasesData>,
    val activeChart: List<CountryChartHistoryItem>,
    val criticalChart: List<CountryChartHistoryItem>,
    val caseDistributionChart: List<CaseStateDistributionItem>
)

data class CountryCasesData(
    val value: String,
    @ColorRes val color: Int
)

data class CountryChartHistoryItem(
    val position: Float,
    val value: Float
)

data class CaseStateDistributionItem(
    val startDate: String,
    val enDate: String,
    val position: Float,
    val nonCritical: Float,
    val critical: Float,
    val recovered: Float,
    val dead: Float
)


