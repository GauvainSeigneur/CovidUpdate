package com.gauvain.seigneur.covidupdate.model

data class CountryHistoryData(
    val activeChart: List<CountryChartHistoryItem>,
    val criticalChart: List<CountryChartHistoryItem>,
    val caseDistributionChart: List<CaseStateDistributionItem>
)

data class CountryChartHistoryItem(
    val position: Float,
    val value: Float
)

data class CaseStateDistributionItem(
    val position: Float,
    val nonCritical: Float,
    val critical: Float,
    val recovered: Float,
    val dead: Float
)


