package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsRepository

internal class FetchStatisticsUseCaseImpl(private val repository: StatisticsRepository) :
    FetchStatisticsUseCase {

    override suspend fun invoke(country: String?): Outcome<List<StatisticsItemModel>, ErrorType> {
        return try {
            val result = repository.statistics(country)
            Outcome.Success(result)
        } catch (e: GetStatisticsException) {
            handleException(e)
        }
    }

    private fun handleException(e: GetStatisticsException): Outcome.Error<ErrorType> =
        when (e.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST-> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.SERVER_INTERNAL_ERROR)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}