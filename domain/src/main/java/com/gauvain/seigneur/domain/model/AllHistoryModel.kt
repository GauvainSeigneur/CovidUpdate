package com.gauvain.seigneur.domain.model

import java.util.*

data class AllHistoryModel(
    val totalCases: Int,
    val totalNewCases: Int?,
    val totalActiveCases: Int,
    val activeHistory: List<AllActiveHistoryItemModel>
)

data class AllActiveHistoryItemModel(
    val total: Int,
    val day: Date
)