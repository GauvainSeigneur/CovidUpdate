package com.gauvain.seigneur.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.model.SharedTransitionState
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
import com.gauvain.seigneur.presentation.utils.StringPresenter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

typealias HistoryState = LiveDataState<CountryHistoryData>

class DetailsViewModel(
    private val fetchCountryHistoryUseCase: FetchCountryHistoryUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    var countryName: String? = null
    val sharedTransitionData = MutableLiveData<SharedTransitionState>()
    val loadingData = MutableLiveData<LoadingState>()
    val historyData: MutableLiveData<HistoryState> by lazy {
        getHistory()
        MutableLiveData<HistoryState>()
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            loadingData.value = LoadingState.INITIAL_IS_LOADING
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
        val result = withContext(Dispatchers.IO) {
            fetchCountryHistoryUseCase.invoke(countryName)
        }

        when (result) {
            is Outcome.Success -> {
                historyData.value = LiveDataState.Success(result.data.toData(numberFormatProvider))
                loadingData.value = LoadingState.INITIAL_IS_LOADED
            }
            is Outcome.Error -> {
                historyData.value = LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.RECOVERABLE,
                        StringPresenter(R.string.error_fetch_data_title),
                        StringPresenter(R.string.error_fetch_data_description),
                        StringPresenter(R.string.retry)
                    )
                )
            }
        }
    }

    private fun manageEmptyName() {
        loadingData.value = LoadingState.INITIAL_IS_LOADED
        historyData.value =
            LiveDataState.Error(
                ErrorData(
                    ErrorDataType.NOT_RECOVERABLE,
                    StringPresenter(R.string.error_no_country_name_title),
                    StringPresenter(R.string.error_no_country_name_desc),
                    StringPresenter(R.string.ok)
                )
            )
    }
}