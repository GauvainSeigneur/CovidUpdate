package com.gauvain.seigneur.presentation.model

import androidx.annotation.ColorRes
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter

data class AllHistoryData(
    val totalCases: String,
    val activeCases: QuantityStringPresenter,
    val newCases: AllHistoryNewCaseData,
    val chart: List<ChartAllHistoryItem>
)

data class AllHistoryNewCaseData(
    val total: StringPresenter,
    @ColorRes
    val colorRes: Int
)

data class ChartAllHistoryItem(
    val position: Float,
    val value: Float
)
