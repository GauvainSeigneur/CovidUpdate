package com.gauvain.seigneur.domain.request

enum class RequestExceptionType {
    UNKNOWN_HOST,
    ERROR_UNKNOWN,
    CONNECTION_LOST,
    UNAUTHORIZED,
    SERVER_INTERNAL_ERROR,
    BODY_NULL
}