package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.BaseProviderException
import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.model.RequestExceptionType
import java.lang.Exception

interface CountryListProvider {
    @Throws(GetCountriesException::class)
    fun getCountryList(): List<CountryItemModel>
}

class GetCountriesException(type: RequestExceptionType,
                            description: String? = null) : BaseProviderException(type, description)


