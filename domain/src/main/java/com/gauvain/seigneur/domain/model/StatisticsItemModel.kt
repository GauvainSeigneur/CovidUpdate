package com.gauvain.seigneur.domain.model

import java.util.*

data class StatisticsItemModel(
    val country: String,
    val casesModel: CasesModel,
    val deathsModel: DeathsModel,
    val day: Date,
    val time: Date
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