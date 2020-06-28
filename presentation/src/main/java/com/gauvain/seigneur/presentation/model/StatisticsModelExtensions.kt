package com.gauvain.seigneur.presentation.model


import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter

fun StatisticsItemModel.toStatisticsItemData(
    code: String?,
    newCasesData: NewCasesData,
    numberFormatProvider: NumberFormatProvider
) =
    StatisticsItemData(
        country = this.country,
        countryCode = code,
        casesData = this.casesModel.toCasesData(numberFormatProvider, newCasesData)
    )

fun CasesModel.toCasesData(
    numberFormatProvider: NumberFormatProvider,
    newCasesData: NewCasesData
) =
    CasesData(
        new = newCasesData,
        active = QuantityStringPresenter(
            R.plurals.active_case_label,
            this.active,
            numberFormatProvider.format(this.active)
        ),
        total = StringPresenter(
            R.string.total_case_label,
            numberFormatProvider.format(this.total)
        )
    )

