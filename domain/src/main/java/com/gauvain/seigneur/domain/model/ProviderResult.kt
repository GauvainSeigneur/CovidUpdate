package com.gauvain.seigneur.domain.model

sealed class ProviderResult<out T, out E : Any> {
    data class Success<out T>(val data: T) : ProviderResult<T, Nothing>()
    data class Error<out E : Any>(val error: E) : ProviderResult<Nothing, E>()
}