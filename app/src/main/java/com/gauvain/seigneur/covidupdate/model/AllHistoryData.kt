package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.ColorRes
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.github.mikephil.charting.data.Entry

data class AllHistoryData(
    val totalCases: String,
    val activeCases: QuantityStringPresenter,
    val newCases: AllHistoryNewCaseData,
    val history: List<AllHistoryItemData>,
    val chart : List<Entry>
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
