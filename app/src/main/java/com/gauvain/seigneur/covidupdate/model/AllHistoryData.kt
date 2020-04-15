package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.covidupdate.utils.StringPresenter

data class AllHistoryData(
    val totalCases: String,
    val newActiveCases: StringPresenter,
    val history: List<AllHistoryItemData>
)

data class AllHistoryItemData(
    val total: Float,
    val day: String,
    val position: Float
)