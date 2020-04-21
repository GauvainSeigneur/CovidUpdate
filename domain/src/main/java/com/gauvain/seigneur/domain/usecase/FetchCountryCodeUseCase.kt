package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.provider.CountryListProvider

interface FetchCountryCodeUseCase {
    fun getCountryCode(countryName: String): String?

    companion object {
        fun create(provider: CountryListProvider): FetchCountryCodeUseCase =
            FetchCountryCodeUseCaseImpl(provider)
    }
}