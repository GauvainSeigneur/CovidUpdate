package com.gauvain.seigneur.domain.model

sealed class Outcome<out T : Any, out E : Any> {
    data class Success<out T : Any>(val data: T) : Outcome<T, Nothing>()
    data class Error<out E : Any>(val error: E) : Outcome<Nothing, E>()
}