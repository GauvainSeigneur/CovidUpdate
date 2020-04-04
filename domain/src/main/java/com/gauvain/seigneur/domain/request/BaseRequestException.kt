package com.gauvain.seigneur.domain.request

 abstract class BaseRequestException(
    type: RequestExceptionType,
    message: String?=null) :Exception()
