package com.gauvain.seigneur.covidupdate.data

sealed class LiveDataState<out T : Any> {
    data class Success<out T : Any>(val data: T) : LiveDataState<T>()
    data class Error(val errorData: ErrorData?=null) : LiveDataState<Nothing>()
}