package com.gauvain.seigneur.data_adapter.model

import com.gauvain.seigneur.domain.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.SERVER_TIME_FORMAT
import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.DeathsModel
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.toDate

fun Stat.toDomainStatistics() = StatisticsModel(
    country = this.country,
    casesModel = this.cases.toDomainCases(),
    deathsModel = this.deaths.toDomainDeaths(),
    day = this.day.toDate(SERVER_DATE_FORMAT),
    time = this.time.toDate(SERVER_TIME_FORMAT)
)

fun Cases.toDomainCases() = CasesModel(
    new = this.new,
    active = this.active,
    critical = this.critical,
    recovered = this.recovered,
    total = this.total
)

fun Deaths.toDomainDeaths() = DeathsModel(
    new = this.new,
    total = this.total
)