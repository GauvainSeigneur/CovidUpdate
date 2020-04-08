package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import java.lang.Exception

interface StatisticsProvider {
    @Throws(GetStatisticsException::class)
    fun statistics(country: String? = null): List<StatisticsItemModel>
}

class GetStatisticsException(
    val type: RequestExceptionType,
    val description: String? = null
) : Exception()
