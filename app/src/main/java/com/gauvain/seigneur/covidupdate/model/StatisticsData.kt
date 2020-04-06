package com.gauvain.seigneur.covidupdate.model

data class StatisticsData(
    val totalData: TotalStatisticsData,
    val mostImpactCountriesData: List<MostImpactCountriesData>,
    val stats : List<StatisticsItemData>
)

data class TotalStatisticsData(
    val totalCases: Int,
    val totalNewCases: Int
)

data class MostImpactCountriesData(
    val name : String,
    val total : Int,
    val share : Double
)

data class StatisticsItemData(
    val country: String,
    val countryCode: String? = null,
    val casesData: CasesData,
    val deathsData: DeathsData,
    val day: String
)

data class CasesData(
    val new: String,
    val active: Int,
    val critical: Int,
    val recovered: Int,
    val total: Int
)

data class DeathsData(
    val new: String,
    val total: Int
)