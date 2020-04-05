package com.gauvain.seigneur.covidupdate.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.data.ErrorData
import com.gauvain.seigneur.covidupdate.data.LiveDataState
import com.gauvain.seigneur.covidupdate.data.StatisticsData
import com.gauvain.seigneur.covidupdate.data.toStatisticsData
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias StatisticsState = LiveDataState<List<StatisticsData>>

class MainViewModel(private val fetchStatisticsUseCase: FetchStatisticsUseCase) :
    ViewModel() {

    val statisLiveItemData: MutableLiveData<StatisticsState> by lazy {
        fetchStatistics()
        MutableLiveData<StatisticsState>()
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
                        statisLiveItemData.value = LiveDataState.Error(ErrorData(
                            null,
                            StringPresenter(R.string.empty_list_title),
                            StringPresenter(R.string.empty_list_description)
                        ))
                    } else {
                        statisLiveItemData.value = LiveDataState.Success(result.data.map {
                            it.toStatisticsData()
                        })
                    }
                }
                is Outcome.Error -> {
                    statisLiveItemData.value = setErrorLiveData(result.error)
                }
            }
        }
    }

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            else -> LiveDataState.Error(ErrorData())
        }
}