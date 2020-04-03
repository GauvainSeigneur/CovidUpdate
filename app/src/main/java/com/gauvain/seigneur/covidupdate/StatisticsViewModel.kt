package com.gauvain.seigneur.covidupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsViewModel(private val fetchStatisticsUseCase: FetchStatisticsUseCase): ViewModel() {

    fun fetchStatistics(country:String?=null) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                fetchStatisticsUseCase.invoke(country)
            }
        }

    }

}