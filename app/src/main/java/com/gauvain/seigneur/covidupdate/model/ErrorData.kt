package com.gauvain.seigneur.covidupdate.model

import androidx.annotation.DrawableRes
import com.gauvain.seigneur.covidupdate.utils.StringPresenter

data class ErrorData(
    val type: ErrorDataType,
    val title: StringPresenter,
    val description: StringPresenter? = null,
    val buttonText: StringPresenter? = null,
    @DrawableRes
    val iconRes: Int? = null
)

enum class ErrorDataType {
    INFORMATIVE,
    RECOVERABLE,
    NOT_RECOVERABLE
}


