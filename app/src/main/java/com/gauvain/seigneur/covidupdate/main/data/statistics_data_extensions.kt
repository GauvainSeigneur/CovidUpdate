package com.gauvain.seigneur.covidupdate.main.data

import com.gauvain.seigneur.domain.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.formatTo
import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.DeathsModel
import com.gauvain.seigneur.domain.model.StatisticsModel

fun StatisticsModel.toStatisticsData() =
    StatisticsData(
        country = this.country,
        casesData = this.casesModel.toCasesData(),
        deathsData = this.deathsModel.toDeathsData(),
        day = this.day.formatTo(DATA_DATE_FORMAT)
    )

fun CasesModel.toCasesData() =
    CasesData(
        new = this.new ?: "NONE",
        active = this.active,
        critical = this.critical,
        recovered = this.recovered,
        total = this.total
    )

fun DeathsModel.toDeathsData() =
    DeathsData(
        new = this.new ?: "NONE",
        total = this.total
    )
