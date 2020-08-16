package com.gauvain.seigneur.presentation.injection

import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { FetchStatisticsUseCase.create(get(), get()) }
    single { FetchAllHistoryUseCase.create(get()) }
    single { FetchCountryCodeUseCase.create(get()) }
    single { FetchCountryHistoryUseCase.create(get()) }
}