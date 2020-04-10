package com.gauvain.seigneur.data_adapter.injection

import com.gauvain.seigneur.data_adapter.adapters.CountryCodeAdapter
import com.gauvain.seigneur.data_adapter.adapters.HistoryAdapter
import com.gauvain.seigneur.data_adapter.adapters.NumberFormatAdapter
import com.gauvain.seigneur.data_adapter.adapters.StatisticsAdapter
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import com.gauvain.seigneur.domain.provider.HistoryProvider
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import org.koin.dsl.module

val adapterModule = module {
    single<StatisticsProvider> {
        StatisticsAdapter(get())
    }

    single<CountryCodeProvider> {
        CountryCodeAdapter(get())
    }

    single<HistoryProvider> {
        HistoryAdapter(get())
    }

    factory<NumberFormatProvider> {
        NumberFormatAdapter()
    }
}