package com.gauvain.seigneur.domain.model

enum class RequestExceptionType {
    UNKNOWN_HOST,
    ERROR_UNKNOWN,
    CONNECTION_LOST,
    UNAUTHORIZED,
    SERVER_INTERNAL_ERROR,
    BODY_NULL,
    VALUE_NULL,
    JSON_ERROR
}

const val EXCEPTION_UNKNOWN_HOST_DESC = "Unknown Host Exception"
const val EXCEPTION_CONNECTION_LOST_DESC = "Connection lost during request"
const val EXCEPTION_ERROR_UNKNOWN_DESC = "Error unknown"
const val EXCEPTION_BODY_NULL_DESC = "Body is null"
const val EXCEPTION_NULL_VALUE_DESC = "Value is null"