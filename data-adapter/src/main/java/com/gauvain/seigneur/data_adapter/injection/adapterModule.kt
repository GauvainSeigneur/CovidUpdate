package com.gauvain.seigneur.data_adapter.injection

import com.gauvain.seigneur.data_adapter.adapters.StatisticsAdapter
import com.gauvain.seigneur.domain.provider.StatisticsRepository
import org.koin.dsl.module

val adapterModule = module {
    single<StatisticsRepository> {
        StatisticsAdapter(get())
    }
}