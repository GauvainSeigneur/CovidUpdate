package com.gauvain.seigneur.covidupdate.injection

import com.gauvain.seigneur.covidupdate.view.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}