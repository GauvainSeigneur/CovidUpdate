package com.gauvain.seigneur.covidupdate.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.utils.LoadingState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
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
    val loadingData = MutableLiveData<LoadingState>()
    val historyData: MutableLiveData<HistoryState> by lazy {
        getHistory()
        MutableLiveData<HistoryState>()
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            loadingData.value = LoadingState.INITIAL_IS_LOADING
            countryName?.let {
                fetchHistory(it)
            } ?: manageEmptyName()
        }
    }

    private suspend fun fetchHistory(countryName: String) {
        val result = withContext(Dispatchers.IO) {
            fetchCountryHistoryUseCase.invoke(countryName)
        }

        when (result) {
            is Outcome.Success -> {
                loadingData.value = LoadingState.INITIAL_IS_LOADED
                historyData.value = LiveDataState.Success(result.data.toData())
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