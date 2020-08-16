package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.*
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

/**
 * Simple methods to:
 * Perform a call to CovidService
 * Handle exception during the call
 * Handle result get from server and return a RequestResult sub class
 */
fun <T : BaseResponse> performCall(call: Call<T>): RequestResult<T, RequestExceptionContent> {
    val result = runCatching {
        call.execute()
    }
        .onFailure {
            val exceptionContent = getRequestExceptionContent(it)
            return RequestResult.Error(
                RequestExceptionContent(
                    exceptionContent.exceptionType,
                    exceptionContent.message
                )
            )
        }
    return handleResult(result)
}

private fun <T : BaseResponse> handleResult(result: Result<Response<T>>): RequestResult<T,
    RequestExceptionContent> {
    result.run {
        getOrNull()?.body()?.let { body ->
            body.message?.let { message ->
                return setError(
                    RequestExceptionType
                        .UNAUTHORIZED, message
                )
            }
            body.response?.let {
                return RequestResult.Success(body)
            }?: return setError(RequestExceptionType.VALUE_NULL, EXCEPTION_NULL_VALUE_DESC)
        } ?: return setError(RequestExceptionType.BODY_NULL, EXCEPTION_BODY_NULL_DESC)
    }
}

private fun setError(type: RequestExceptionType, message: String) =
    RequestResult.Error(RequestExceptionContent(type, message))

private fun getRequestExceptionContent(throwable: Throwable): RequestExceptionContent =
    when (throwable) {
        is UnknownHostException ->
            RequestExceptionContent(
                RequestExceptionType.UNKNOWN_HOST,
                EXCEPTION_UNKNOWN_HOST_DESC
            )
        is UnknownError -> RequestExceptionContent(
            RequestExceptionType.ERROR_UNKNOWN,
            EXCEPTION_ERROR_UNKNOWN_DESC
        )
        is IOException -> RequestExceptionContent(
            RequestExceptionType.CONNECTION_LOST,
            EXCEPTION_CONNECTION_LOST_DESC
        )
        else -> {
            RequestExceptionContent(
                RequestExceptionType.ERROR_UNKNOWN,
                EXCEPTION_ERROR_UNKNOWN_DESC
            )
        }
    }


