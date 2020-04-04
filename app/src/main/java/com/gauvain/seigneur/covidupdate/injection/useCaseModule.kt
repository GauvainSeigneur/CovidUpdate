package com.gauvain.seigneur.covidupdate.injection

import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { FetchStatisticsUseCase.create(get()) }
}