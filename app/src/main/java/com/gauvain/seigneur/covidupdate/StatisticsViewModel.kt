package com.gauvain.seigneur.covidupdate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.domain.request.Outcome
import com.gauvain.seigneur.domain.model.StatisticsModel
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsViewModel(private val fetchStatisticsUseCase: FetchStatisticsUseCase) :
    ViewModel() {

    val statisticsModel: MutableLiveData<List<StatisticsModel>> by lazy {
        fetchStatistics()
        MutableLiveData<List<StatisticsModel>>()
    }
    val loadingState: MutableLiveData<RequestState> = MutableLiveData()

    private fun fetchStatistics(country: String? = null) {
        viewModelScope.launch {
            loadingState.value = RequestState.IS_LOADING
            val result = withContext(Dispatchers.IO) {
                fetchStatisticsUseCase.invoke(country)
            }
            loadingState.value = RequestState.IS_LOADED
            when (result) {
                is Outcome.Success -> {
                    if (result.data.isEmpty()) {
                        loadingState.value = RequestState.IS_EMPTY
                    } else {
                        statisticsModel.value = result.data
                    }
                }
                is Outcome.Error -> {
                    loadingState.value = RequestState.IS_IN_ERROR
                }
            }
        }
    }
}