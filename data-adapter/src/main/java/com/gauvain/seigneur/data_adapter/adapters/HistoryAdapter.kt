package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_BODY_NUL_DESC
import com.gauvain.seigneur.data_adapter.utils.getRequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import retrofit2.Response

class HistoryAdapter(val service: CovidService) :
    HistoryProvider {

    override fun history(country:String): List<StatisticsItemModel> {
        val result = runCatching {
            service.history(country).execute()
        }.onFailure {
            val exceptionContent = getRequestExceptionContent(
                it
            )
            throw GetHistoryException(exceptionContent.exceptionType, exceptionContent.message)
        }
        return handleResult(result)
    }

    private fun handleResult(result: Result<Response<Statistics>>): List<StatisticsItemModel> {
        return result.run {
            getOrNull()?.body().let {
                it?.message?.let { message ->
                    throw GetHistoryException(RequestExceptionType.UNAUTHORIZED, message)
                }
                it?.stats?.map { stat ->
                    stat.toDomainStatistics()
                }
            } ?: throw GetHistoryException(RequestExceptionType.BODY_NULL, EXCEPTION_BODY_NUL_DESC)
        }
    }
}