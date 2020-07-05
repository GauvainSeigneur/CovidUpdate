package com.gauvain.seigneur.data_adapter.model

import com.gauvain.seigneur.domain.utils.toDate
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.SERVER_TIME_FORMAT

fun Stat.toDomainStatistics() = StatisticsItemModel(
    country = this.country,
    casesModel = this.cases.toDomainCases(),
    deathsModel = this.deaths.toDomainDeaths(),
    day = this.day.toDate(SERVER_DATE_FORMAT),
    time = this.time.toDate(SERVER_TIME_FORMAT)
)

fun Cases.toDomainCases() = CasesModel(
    new = getNewCasesInfo(this.new),
    active = this.active,
    critical = this.critical,
    recovered = this.recovered,
    total = this.total
)

fun Deaths.toDomainDeaths() = DeathsModel(
    new = this.new,
    total = this.total
)

private fun getNewCasesInfo(value: String?): Int =
    value?.let {
        it.removePrefix("+").toInt()
    } ?: 0
