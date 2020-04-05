package com.gauvain.seigneur.data_adapter.utils

import com.gauvain.seigneur.data_adapter.model.BaseResponse
import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.domain.model.RequestExceptionType

fun <T>getExceptionFromResult(t: T?): RequestExceptionContent? =
    t?.let {
        if (t is BaseResponse) {
            t.message?.let {
                RequestExceptionContent(RequestExceptionType.UNAUTHORIZED, it)
            }
        } else {
            null
        }
    } ?: RequestExceptionContent(RequestExceptionType.BODY_NULL, "Null value")


fun <T> handleResponse(t: T?):Pair<RequestExceptionContent?, T?> =
    t?.let {
        if (t is BaseResponse) {
            t.message?.let {
                Pair(RequestExceptionContent(RequestExceptionType.UNAUTHORIZED, it), null)
            }?: Pair(null, t as? T)
        } else {
            Pair(RequestExceptionContent(RequestExceptionType.UNKNOWN_OBJECT, "Object is unknown"),
                null)
        }
    } ?: Pair(RequestExceptionContent(RequestExceptionType.BODY_NULL, "Null value"), null)
