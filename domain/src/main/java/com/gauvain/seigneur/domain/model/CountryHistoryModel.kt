package com.gauvain.seigneur.domain.model

import java.util.*

data class CountryHistoryModel(
    val country: String,
    val history:List<CountryHistoryItemModel>
)

data class CountryHistoryItemModel(
    val date: Date,
    val total: Int,
    val new: Int,
    val active: Int,
    val recovered: Int,
    val critical: Int
)