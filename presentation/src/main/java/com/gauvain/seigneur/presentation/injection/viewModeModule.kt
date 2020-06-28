package com.gauvain.seigneur.presentation.injection

import com.gauvain.seigneur.presentation.DetailsViewModel
import com.gauvain.seigneur.presentation.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }
}