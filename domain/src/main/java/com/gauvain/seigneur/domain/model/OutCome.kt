package com.gauvain.seigneur.domain.model

sealed class Outcome<out T, out E : Any> {
    data class Success<out T>(val data: T) : Outcome<T, Nothing>()
    data class Error<out E : Any>(val error: E) : Outcome<Nothing, E>()
}