package com.gauvain.seigneur.covidupdate.model.base

import com.gauvain.seigneur.covidupdate.model.ErrorData

sealed class LiveDataState<out T : Any> {
    data class Success<out T : Any>(val data: T) : LiveDataState<T>()
    data class Error(val errorData: ErrorData) : LiveDataState<Nothing>()
}