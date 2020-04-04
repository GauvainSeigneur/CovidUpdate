package com.gauvain.seigneur.data_adapter.adapters

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.data_adapter.model.toDomainStatistics
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.domain.request.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsRepository
import com.gauvain.seigneur.domain.model.StatisticsModel
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class StatisticsAdapter(val service: CovidService) :
    StatisticsRepository {

    override fun statistics(country: String?): List<StatisticsModel> {
        val result = runCatching {
            service.statistics(country).execute()
        }.onFailure {
            handleFailure(it)
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

    private fun handleFailure(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException -> throw GetStatisticsException(
                RequestExceptionType.UNKNOWN_HOST,
                "Unknown Host Exception"
            )
            is UnknownError -> throw GetStatisticsException(
                RequestExceptionType.ERROR_UNKNOWN,
                "Error unknwon"
            )
            is IOException -> throw GetStatisticsException(
                RequestExceptionType.CONNECTION_LOST,
                "Connection lost during request"
            )
            else -> {
                throw GetStatisticsException(
                    RequestExceptionType.ERROR_UNKNOWN,
                    "Error unknwon"
                )
            }
        }
    }
}