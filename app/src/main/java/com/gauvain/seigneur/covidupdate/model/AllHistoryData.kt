package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.ColorRes
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.github.mikephil.charting.data.Entry
import java.text.FieldPosition

data class AllHistoryData(
    val totalCases: String,
    val activeCases: QuantityStringPresenter,
    val newCases: AllHistoryNewCaseData,
    val history: List<AllHistoryItemData>,
    val chart: List<ChartAllHistoryItem>
)

data class AllHistoryNewCaseData(
    val total: StringPresenter,
    @ColorRes
    val colorRes: Int
)

data class AllHistoryItemData(
    val total: Int,
    val day: String
)

data class ChartAllHistoryItem(
    val position: Float,
    val value: Float
)
