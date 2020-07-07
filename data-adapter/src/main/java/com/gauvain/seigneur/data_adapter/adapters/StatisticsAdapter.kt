package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.data_adapter.utils.performCall
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import com.gauvain.seigneur.domain.model.StatisticsItemModel

class StatisticsAdapter(private val service: CovidService) :
    StatisticsProvider {

    override fun statistics(country: String?): List<StatisticsItemModel> =
        when (val result = performCall(service.statistics(country))) {
            is RequestResult.Success -> {
                result.data.stats.map { stat -> stat.toDomainStatistics() }
            }
            is RequestResult.Error -> {
                throw GetStatisticsException(result.error.exceptionType, result.error.message)
            }
        }
}

