package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.request.RequestExceptionType
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.request.BaseRequestException

interface StatisticsRepository {
    @Throws(GetStatisticsException::class)
    fun statistics(country: String? = null): List<StatisticsModel>
}

class GetStatisticsException(
    type: RequestExceptionType,
    message: String? = null
) : BaseRequestException(type, message)
