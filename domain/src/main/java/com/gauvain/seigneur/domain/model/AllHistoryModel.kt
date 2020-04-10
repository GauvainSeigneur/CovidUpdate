package com.gauvain.seigneur.domain.model

import java.util.*

data class AllHistoryModel(
    val totalCases: Int,
    val totalNewCases: Int?,
    val history: List<AllHistoryItemModel>
)

data class AllHistoryItemModel(
    val total: Int,
    val day: Date
)