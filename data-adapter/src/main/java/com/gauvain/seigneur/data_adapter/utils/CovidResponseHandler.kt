package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.BaseResponse
import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.domain.model.RequestExceptionType
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
fun <T> performCall(call: Call<T>): RequestResult<T, RequestExceptionContent> {
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
    return handleRequestResult(result.getOrElse {
        return RequestResult.Error(
            RequestExceptionContent(
                RequestExceptionType
                    .RESULT_NULL, EXCEPTION_NULL_VALUE_DESC
            )
        )
    })
}

//Suppress cast check because is already handled by retrofit in case of success
@Suppress("UNCHECKED_CAST")
private fun <T> handleRequestResult(result: Response<T>): RequestResult<T,
    RequestExceptionContent> =
    result.body()?.let { body ->
        (body as? BaseResponse)?.let { baseResponse ->
            baseResponse.message?.let {
                setError(RequestExceptionType.UNAUTHORIZED, it)
            } ?: RequestResult.Success(body as T)
        } ?: setError(RequestExceptionType.UNKNOWN_OBJECT, EXCEPTION_UNKNOWN_OBJECT_DESC)
    } ?: setError(RequestExceptionType.BODY_NULL, EXCEPTION_BODY_NULL_DESC)

private fun setError(type: RequestExceptionType, message: String):
    RequestResult.Error<RequestExceptionContent> = RequestResult.Error(
    RequestExceptionContent(
        type,
        message
    )
)

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

const val EXCEPTION_UNKNOWN_HOST_DESC = "Unknown Host Exception"
const val EXCEPTION_CONNECTION_LOST_DESC = "Connection lost during request"
const val EXCEPTION_ERROR_UNKNOWN_DESC = "Error unknown"
const val EXCEPTION_BODY_NULL_DESC = "Body is null"
const val EXCEPTION_NULL_VALUE_DESC = "Value is null"
const val EXCEPTION_UNKNOWN_OBJECT_DESC = "unknown object"

