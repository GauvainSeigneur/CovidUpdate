package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_BODY_NUL_DESC
import com.gauvain.seigneur.data_adapter.utils.getRequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import retrofit2.Response

class StatisticsAdapter(private val service: CovidService) :
    StatisticsProvider {

    override fun statistics(country: String?): List<StatisticsItemModel> {
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

    private fun handleResult(result: Result<Response<Statistics>>): List<StatisticsItemModel> {
        return result.run {
            getOrNull()?.body().let {
                it?.message?.let { message ->
                    throw GetStatisticsException(RequestExceptionType.UNAUTHORIZED, message)
                }
                it?.stats?.map { stat ->
                    stat.toDomainStatistics()
                }
            } ?: throw GetStatisticsException(
                RequestExceptionType.BODY_NULL,
                EXCEPTION_BODY_NUL_DESC
            )
        }
    }
}