package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.DrawableRes
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

data class ErrorData(
    val title: StringPresenter,
    val description: StringPresenter? = null,
    @DrawableRes
    val iconRes: Int? = null
)