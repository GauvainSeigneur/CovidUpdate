package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import java.lang.Exception

interface HistoryProvider {
    @Throws(GetHistoryException::class)
    fun history(country:String): List<StatisticsItemModel>
}

class GetHistoryException(
    val type: RequestExceptionType,
    val description: String? = null
) : Exception()
