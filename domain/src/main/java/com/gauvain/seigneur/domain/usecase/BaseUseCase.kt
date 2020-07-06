package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.*

internal abstract class BaseUseCase<in E> {

    /**
     * Map exception into a dedicated error
     */
    fun handleException(e: E): Outcome.Error<ErrorType> =
        when ((e as? BaseProviderException)?.type) {
            RequestExceptionType.UNKNOWN_HOST -> Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST)
            RequestExceptionType.CONNECTION_LOST -> Outcome.Error(ErrorType.ERROR_CONNECTION_LOST)
            RequestExceptionType.UNAUTHORIZED -> Outcome.Error(ErrorType.ERROR_UNAUTHORIZED)
            RequestExceptionType.SERVER_INTERNAL_ERROR -> Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER)
            else -> Outcome.Error(ErrorType.ERROR_UNKNOWN)
        }
}