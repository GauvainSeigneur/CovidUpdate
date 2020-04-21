package com.gauvain.seigneur.domain.provider

import com.gauvain.seigneur.domain.model.CountryItemModel
import java.lang.Exception

interface CountryListProvider {
    @Throws(GetCountriesException::class)
    fun getCountryList(): List<CountryItemModel>
}

class GetCountriesException : Exception()


