package com.gauvain.seigneur.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter

data class StatisticsItemData(
    val country: String,
    val countryCode: String? = null,
    val casesData: CasesData
)

data class CasesData(
    val new: NewCasesData,
    val active: QuantityStringPresenter,
    val total: StringPresenter
)

data class NewCasesData(
    val total: StringPresenter,
    @DrawableRes
    val icon: Int? = null,
    @ColorRes
    val color: Int
)
