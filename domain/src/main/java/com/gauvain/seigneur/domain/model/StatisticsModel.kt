package com.gauvain.seigneur.domain.model

data class StatisticsModel(
    val country: String,
    val casesModel: CasesModel,
    val deathsModel: DeathsModel,
    val day: String,
    val time: String
)

data class CasesModel(
    val new: String? = null,
    val active: Int,
    val critical: Int,
    val recovered: Int,
    val total: Int
)

data class DeathsModel(
    val new: String? = null,
    val total: Int
)