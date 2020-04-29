package com.gauvain.seigneur.domain.usecase

import com.gauvain.seigneur.domain.model.CountryHistoryModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.HistoryProvider

interface FetchCountryHistoryUseCase {
    suspend fun invoke(countryName: String): Outcome<CountryHistoryModel, ErrorType>

    companion object {
        fun create(provider: HistoryProvider): FetchCountryHistoryUseCase =
            FetchCountryHistoryUseCaseImpl(provider)
    }
}