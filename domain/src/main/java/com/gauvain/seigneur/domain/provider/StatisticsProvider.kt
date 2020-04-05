package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.StatisticsModel
import java.lang.Exception

interface StatisticsRepository {
    @Throws(GetStatisticsException::class)
    fun statistics(country: String? = null): List<StatisticsModel>
}

class GetStatisticsException(
    val type: RequestExceptionType,
    val description: String? = null
) : Exception()
