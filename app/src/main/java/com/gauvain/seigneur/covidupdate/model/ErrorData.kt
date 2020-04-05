package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.DrawableRes
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

data class ErrorData(
    @DrawableRes val iconRes:Int?=null,
    val title: StringPresenter? = null,
    val description: StringPresenter? = null
)