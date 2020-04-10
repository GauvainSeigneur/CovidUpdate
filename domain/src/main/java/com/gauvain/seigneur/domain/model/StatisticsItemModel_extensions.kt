package com.gauvain.seigneur.domain.model

fun StatisticsItemModel.toAllHistoryItemModel() = AllHistoryItemModel(
    total = this.casesModel.total,
    day = this.day
)
