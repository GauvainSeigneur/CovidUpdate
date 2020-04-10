package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType
import java.io.IOException
import java.net.UnknownHostException

const val EXCEPTION_UNKNOWN_HOST_DESC = "Unknown Host Exception"
const val EXCEPTION_CONNECTION_LOST_DESC = "Connection lost during request"
const val EXCEPTION_ERROR_UNKNOWN_DESC = "Error unknown"
const val EXCEPTION_BODY_NUL_DESC = "Body is null"

fun getRequestExceptionContent(throwable: Throwable): RequestExceptionContent =
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