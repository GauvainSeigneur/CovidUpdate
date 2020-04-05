package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType
import java.io.IOException
import java.net.UnknownHostException

fun getRequestExceptionContent(throwable: Throwable): RequestExceptionContent =
    when (throwable) {
        is UnknownHostException -> RequestExceptionContent(
            RequestExceptionType.UNKNOWN_HOST,
            "Unknown Host Exception"
        )
        is UnknownError -> RequestExceptionContent(
            RequestExceptionType.ERROR_UNKNOWN,
            "Error unknwon"
        )
        is IOException -> RequestExceptionContent(
            RequestExceptionType.CONNECTION_LOST,
            "Connection lost during request"
        )
        else -> {
            RequestExceptionContent(
                RequestExceptionType.ERROR_UNKNOWN,
                "Error unknwon"
            )
        }
    }