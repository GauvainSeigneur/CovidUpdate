package com.gauvain.seigneur.data_adapter.model

typealias DomainStatistics = com.gauvain.seigneur.domain.model.Statistics
typealias DomainCases = com.gauvain.seigneur.domain.model.Cases
typealias DomainDeaths = com.gauvain.seigneur.domain.model.Deaths

fun Stat.toDomainStatistics() = DomainStatistics(
    country = this.country,
    cases = this.cases.toDomainCases(),
    deaths = this.deaths.toDomainDeaths(),
    day = this.day,
    time = this.time
)

fun Cases.toDomainCases() = DomainCases(
    new = this.new,
    active = this.active,
    critical = this.critical,
    recovered = this.recovered,
    total = this.total
)

fun Deaths.toDomainDeaths() = DomainDeaths(
    new = this.new,
    total = this.total
)