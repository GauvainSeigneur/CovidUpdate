package com.gauvain.seigneur.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.SharedTransitionState
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.ioJob

typealias HistoryState = LiveDataState<CountryHistoryData>

class DetailsViewModel(
    private val fetchCountryHistoryUseCase: FetchCountryHistoryUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel() {

    var countryName: String? = null
    val sharedTransitionData = MutableLiveData<SharedTransitionState>()
    val loadingData = MutableLiveData<LoadingState>()
    val historyData: MutableLiveData<HistoryState> by lazy {
        getHistory()
        MutableLiveData<HistoryState>()
    }

    fun getHistory() {
        ioJob {
            loadingData.postValue(LoadingState.INITIAL_IS_LOADING)
            countryName?.let { fetchHistory(it) } ?: manageEmptyName()
        }
    }

    fun onSharedTransitionStart() {
        sharedTransitionData.value = SharedTransitionState.STARTED
    }

    fun onSharedTransitionEnd() {
        sharedTransitionData.value = SharedTransitionState.ENDED
    }

    private suspend fun fetchHistory(countryName: String) {
        val result = fetchCountryHistoryUseCase.invoke(countryName)
        when (result) {
            is Outcome.Success -> {
                historyData.postValue(LiveDataState.Success(result.data.toData(numberFormatProvider)))
                loadingData.postValue(LoadingState.INITIAL_IS_LOADED)
            }
            is Outcome.Error -> {
                historyData.postValue(
                    LiveDataState.Error(
                        ErrorData(
                            ErrorDataType.RECOVERABLE,
                            StringPresenter(R.string.error_fetch_data_title),
                            StringPresenter(R.string.error_fetch_data_description),
                            StringPresenter(R.string.retry)
                        )
                    )
                )
            }
        }
    }

    private fun manageEmptyName() {
        loadingData.postValue(LoadingState.INITIAL_IS_LOADED)
        historyData.postValue(
            LiveDataState.Error(
                ErrorData(
                    ErrorDataType.NOT_RECOVERABLE,
                    StringPresenter(R.string.error_no_country_name_title),
                    StringPresenter(R.string.error_no_country_name_desc),
                    StringPresenter(R.string.ok)
                )
            )
        )
    }
}