package com.gauvain.seigneur.domain.model

abstract class BaseProviderException(
    val type: RequestExceptionType,
    val description: String?
) : Exception()