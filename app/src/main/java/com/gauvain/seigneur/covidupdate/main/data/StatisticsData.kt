package com.gauvain.seigneur.covidupdate.main.data

data class StatisticsData(
    val country: String,
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