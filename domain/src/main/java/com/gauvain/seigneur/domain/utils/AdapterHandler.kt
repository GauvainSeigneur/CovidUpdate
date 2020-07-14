package com.gauvain.seigneur.domain.utils

import com.gauvain.seigneur.domain.model.*
import kotlin.reflect.KClass

/**
 * Use KClass in order to avoid type method in useCase like this
 * val result = callProvider<List<StatisticsItem>, GetStatisticsException>
 * it also makes the use of BaseProviderException mandatory
 */
fun <T> callProvider(providerCall: () -> T, exception : KClass<out BaseProviderException>)
    : ProviderResult<T, ErrorType> {
    return try {
        val result = providerCall()
        ProviderResult.Success(result)
    } catch (e: BaseProviderException) {
        if (exception.isInstance(value = e)) {
            handleProviderException(e)
        } else {
            //throw it as we miss something in UseCase or Adapter
            throw e
        }
    }
}

private fun <E : BaseProviderException> handleProviderException(e: E): ProviderResult
.Error<ErrorType> =
    when (e.type) {
        RequestExceptionType.UNKNOWN_HOST -> ProviderResult.Error(ErrorType.ERROR_UNKNOWN_HOST)
        RequestExceptionType.CONNECTION_LOST -> ProviderResult.Error(ErrorType.ERROR_CONNECTION_LOST)
        RequestExceptionType.UNAUTHORIZED -> ProviderResult.Error(ErrorType.ERROR_UNAUTHORIZED)
        RequestExceptionType.SERVER_INTERNAL_ERROR -> ProviderResult.Error(ErrorType.ERROR_INTERNAL_SERVER)
        else -> ProviderResult.Error(ErrorType.ERROR_UNKNOWN)
    }