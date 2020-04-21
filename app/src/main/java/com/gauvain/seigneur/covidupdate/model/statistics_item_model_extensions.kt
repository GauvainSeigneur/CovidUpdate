package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider

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

