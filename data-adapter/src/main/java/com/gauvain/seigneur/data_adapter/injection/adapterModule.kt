package com.gauvain.seigneur.data_adapter.injection

import com.gauvain.seigneur.data_adapter.adapters.*
import com.gauvain.seigneur.domain.provider.*
import org.koin.dsl.module

val adapterModule = module {
    single<StatisticsProvider> {
        StatisticsAdapter(get())
    }

    single<CountryListProvider> {
        CountryListAdapter(get())
    }

    single<HistoryProvider> {
        HistoryAdapter(get())
    }

    factory<NumberFormatProvider> {
        NumberFormatAdapter()
    }
}