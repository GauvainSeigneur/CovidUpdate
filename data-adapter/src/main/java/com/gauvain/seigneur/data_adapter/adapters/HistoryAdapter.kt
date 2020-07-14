package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.*
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider

class HistoryAdapter(private val service: CovidService) :
    HistoryProvider {

    override fun history(country: String): List<StatisticsItemModel> =
        when (val result = performCall(service.history(country))) {
            is RequestResult.Success -> {
                result.data.response.map { stat -> stat.toDomainStatistics() }
            }
            is RequestResult.Error -> {
                throw GetHistoryException(result.error.exceptionType, result.error.message)
            }
        }
}