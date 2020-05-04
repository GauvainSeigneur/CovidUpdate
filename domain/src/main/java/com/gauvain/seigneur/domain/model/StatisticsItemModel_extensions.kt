package com.gauvain.seigneur.domain.model

fun StatisticsItemModel.toAllActiveHistoryItemModel() = AllActiveHistoryItemModel(
    total = this.casesModel.active,
    day = this.day
)

fun StatisticsItemModel.toCountryHistoryItemModel() = CountryHistoryItemModel(
    date = this.day,
    total = this.casesModel.total,
    new = this.casesModel.new ?: 0,
    active = this.casesModel.active,
    recovered = this.casesModel.recovered,
    critical = this.casesModel.critical,
    dead = this.deathsModel.total
)
