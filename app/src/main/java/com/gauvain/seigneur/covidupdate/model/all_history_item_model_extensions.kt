package com.gauvain.seigneur.covidupdate.model

import com.gauvain.seigneur.domain.model.AllHistoryItemModel
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo

fun AllHistoryItemModel.toData(position:Float) =
    AllHistoryItemData(
        total = this.total.toFloat(),
        day = this.day.formatTo(DATA_DATE_FORMAT),
        position = position
    )
