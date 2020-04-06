package com.gauvain.seigneur.covidupdate.model

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