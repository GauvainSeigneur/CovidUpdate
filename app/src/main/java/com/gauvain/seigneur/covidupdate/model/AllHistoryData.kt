package com.gauvain.seigneur.covidupdate.model

data class AllHistoryData(
    val totalCases: String,
    val totalNewCases: String,
    val history: List<AllHistoryItemData>
)

data class AllHistoryItemData(
    val total: Float,
    val day: String,
    val position: Float
)