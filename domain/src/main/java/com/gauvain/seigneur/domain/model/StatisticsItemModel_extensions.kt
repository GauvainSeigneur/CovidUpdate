package com.gauvain.seigneur.domain.model

fun StatisticsItemModel.toAllHistoryItemModel() = AllActiveHistoryItemModel(
    total = this.casesModel.active,
    day = this.day
)
