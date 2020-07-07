package com.gauvain.seigneur.data_adapter.model

sealed class RequestResult<out T, out E : Any> {
    data class Success<out T>(val data: T) : RequestResult<T, Nothing>()
    data class Error<out E : Any>(val error: E) : RequestResult<Nothing, E>()
}