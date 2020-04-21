package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

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
