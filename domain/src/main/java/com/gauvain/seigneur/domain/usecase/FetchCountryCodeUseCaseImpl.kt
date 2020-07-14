package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.CountryItemModel
import com.gauvain.seigneur.domain.model.ProviderResult
import com.gauvain.seigneur.domain.provider.CountryListProvider
import com.gauvain.seigneur.domain.provider.GetCountriesException
import com.gauvain.seigneur.domain.utils.callProvider
import java.util.*

internal class FetchCountryCodeUseCaseImpl(private val provider: CountryListProvider) :
    FetchCountryCodeUseCase {

    companion object {
        private val LOCAL = Locale.US
    }

    override fun getCountryCode(countryName: String): String? =
        when (
            val result = callProvider(
                { provider.getCountryList() },
                GetCountriesException::class
            )) {
            is ProviderResult.Success -> {
                fetchCountryCode(countryName, result.data)
            }
            is ProviderResult.Error -> {
                null
            }
        }

    private fun fetchCountryCode(countryName: String, countries: List<CountryItemModel>): String? {
        var code: String? = null
        for (item in countries) {
            if (countryName.toLowerCase(LOCAL) == item.country.toLowerCase(LOCAL)) {
                code = item.code.toLowerCase(LOCAL)
            }
        }
        return code
    }
}