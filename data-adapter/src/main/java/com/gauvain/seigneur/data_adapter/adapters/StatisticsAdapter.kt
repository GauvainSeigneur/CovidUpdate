package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.getRequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsRepository
import com.gauvain.seigneur.domain.model.StatisticsModel
import retrofit2.Response

class StatisticsAdapter(val service: CovidService) :
    StatisticsRepository {

    override fun statistics(country: String?): List<StatisticsModel> {
        val result = runCatching {
            service.statistics(country).execute()
        }.onFailure {
            val exceptionContent = getRequestExceptionContent(
                it
            )
            throw GetStatisticsException(exceptionContent.exceptionType, exceptionContent.message)
        }
        return handleResult(result)
    }

    private fun handleResult(result: Result<Response<Statistics>>): List<StatisticsModel> {
        return result.run {
            getOrNull()?.body().run {
                this?.message?.let {
                    throw GetStatisticsException(RequestExceptionType.UNAUTHORIZED, it)
                }
                this?.stats?.map { stat ->
                    stat.toDomainStatistics()
                }
            } ?: throw GetStatisticsException(RequestExceptionType.BODY_NULL, "Null value")
        }
    }
}