package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.ColorRes
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

data class AllHistoryData(
    val totalCases: String,
    val activeCases: QuantityStringPresenter,
    val newCases: AllHistoryNewCaseData,
    val history: List<AllHistoryItemData>
)

data class AllHistoryNewCaseData(
    val total: StringPresenter,
    @ColorRes
    val colorRes: Int
)

data class AllHistoryItemData(
    val total: Float,
    val day: String,
    val position: Float
)