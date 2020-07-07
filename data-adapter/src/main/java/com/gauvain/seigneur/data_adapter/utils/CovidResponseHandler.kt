package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.BaseResponse
import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.data_adapter.model.RequestResult
import com.gauvain.seigneur.domain.model.RequestExceptionType
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

fun <T> performCall(call: Call<T>): RequestResult<T, RequestExceptionContent> {
    val result = runCatching {
        call.execute()
    }.onFailure {
        val exceptionContent = getRequestExceptionContent(it)
        return RequestResult.Error(RequestExceptionContent(
                exceptionContent.exceptionType,
                exceptionContent.message
            )
        )
    }
    return handleRequestResult(result.getOrNull())
}

private fun <T> handleRequestResult(result: Response<T>?): RequestResult<T,
    RequestExceptionContent> =
    result?.let { response ->
        response.body().let { type ->
            (type as? BaseResponse)?.let { baseResponse ->
                baseResponse.message?.let {
                    RequestResult.Error(
                        RequestExceptionContent(
                            RequestExceptionType
                                .UNAUTHORIZED, it
                        )
                    )
                } ?: RequestResult.Success(type as T)
            } ?: RequestResult.Error(
                RequestExceptionContent(
                    RequestExceptionType.UNKNOWN_OBJECT,
                    "Object is unknown"
                )
            )
        }
    } ?: RequestResult.Error(RequestExceptionContent(RequestExceptionType.BODY_NULL, "Null value"))


private fun getRequestExceptionContent(throwable: Throwable): RequestExceptionContent =
    when (throwable) {
        is UnknownHostException -> RequestExceptionContent(
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
const val EXCEPTION_BODY_NUL_DESC = "Body is null"