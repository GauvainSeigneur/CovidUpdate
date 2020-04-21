package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.provider.CountryListProvider
import com.gauvain.seigneur.domain.provider.GetCountriesException
import java.util.*

internal class FetchCountryCodeUseCaseImpl(private val provider: CountryListProvider) :
    FetchCountryCodeUseCase {

    override fun getCountryCode(countryName: String): String? {
        var countryCode: String?
        try {
            val countries = provider.getCountryList()
            countryCode = fetchCountryCode(countryName, countries)
        } catch (e: GetCountriesException) {
            countryCode = null
        }
        return countryCode
    }

    private fun fetchCountryCode(countryName: String, countries: List<CountryItemModel>): String? {
        var code: String? = null
        for (item in countries) {
            if (countryName.toLowerCase(Locale.ENGLISH) == item.country.toLowerCase(Locale.ENGLISH)) {
                code = item.code.toLowerCase(Locale.ENGLISH)
            }
        }
        return code
    }
}