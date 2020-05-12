package com.gauvain.seigneur.covidupdate.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.model.LoadingState
import com.gauvain.seigneur.covidupdate.model.base.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.covidupdate.utils.event.Event
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

typealias StatisticsState = LiveDataState<List<StatisticsItemData>>
typealias AllHistoryState = LiveDataState<AllHistoryData>
typealias RefreshEventState = Event<LiveDataState<StringPresenter>>

class MainViewModel(
    private val fetchStatisticsUseCase: FetchStatisticsUseCase,
    private val fetchAllHistoryUseCase: FetchAllHistoryUseCase,
    private val fetchCountryCodeUseCase: FetchCountryCodeUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel(), CoroutineScope {

    companion object {
        const val LONG_DELAY = 400L
        const val SMALL_DELAY = 350L
        const val NO_DELAY = 0L
    }

    //LiveData
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val historyData: MutableLiveData<AllHistoryState> = MutableLiveData()
    val statisticsData = MutableLiveData<StatisticsState>()
    //Event (LiveData which can be consumed only once)
    val refreshDataEvent = MutableLiveData<RefreshEventState>()
    //Define coroutine context
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            fetchStatistics()
        }
        viewModelScope.launch(Dispatchers.Main) {
            fetchHistory()
        }
    }

    fun refreshData() {
        loadingState.value = LoadingState.REFRESH_IS_LOADING
        viewModelScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            fetchStatistics()
        }
        viewModelScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            fetchHistory()
        }
    }

    private suspend fun fetchStatistics() {
        val isRefreshData = statisticsData.value != null && statisticsData.value is
            LiveDataState.Success
        val resultDelay: Long
        when (isRefreshData) {
            true -> {
                resultDelay = SMALL_DELAY
            }
            else -> {
                loadingState.value = LoadingState.INITIAL_IS_LOADING
                resultDelay = NO_DELAY
            }
        }
        val result = getStatisticsOutcome(isRefreshData)
        delay(resultDelay)
        when (result) {
            is Outcome.Success -> {
                handleStatisticsOutComeSuccess(result, isRefreshData)
            }
            is Outcome.Error -> {
                handleStatisticsOutcomeError(result, isRefreshData)
            }
        }
    }

    private suspend fun fetchHistory() {
        val result = withContext(Dispatchers.IO) {
            fetchAllHistoryUseCase.invoke()
        }
        when (result) {
            is Outcome.Success -> {
                historyData.value = LiveDataState.Success(
                    result.data.toData(numberFormatProvider)
                )
            }
        }
    }

    private suspend fun getStatisticsOutcome(
        isRefreshing: Boolean,
        country: String? = null
    ): Outcome<List<StatisticsItemModel>, ErrorType> {
        val result = withContext(Dispatchers.IO) {
            fetchStatisticsUseCase.invoke(country)
        }
        when (isRefreshing) {
            true -> loadingState.value = LoadingState.REFRESH_IS_LOADED
            else -> loadingState.value = LoadingState.INITIAL_IS_LOADED
        }
        return result
    }

    private fun handleStatisticsOutcomeError(
        result: Outcome.Error<ErrorType>,
        isRefreshing: Boolean
    ) {
        if (isRefreshing) {
            refreshDataEvent.value = Event(
                LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.INFORMATIVE, StringPresenter(
                            R.string.error_refresh_data_label
                        )
                    )
                )
            )
        } else {
            statisticsData.value = setErrorLiveData(result.error)
        }
    }

    private fun handleStatisticsOutComeSuccess(
        result: Outcome.Success<List<StatisticsItemModel>>,
        isRefreshing: Boolean
    ) {
        if (result.data.isEmpty()) {
            if (isRefreshing) {
                refreshDataEvent.value =
                    Event(
                        LiveDataState.Error(
                            ErrorData(
                                ErrorDataType.INFORMATIVE, StringPresenter(
                                    R.string.error_refresh_data_label
                                )
                            )
                        )
                    )
            } else {
                statisticsData.value = LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.NOT_RECOVERABLE,
                        StringPresenter(R.string.empty_list_title),
                        StringPresenter(R.string.empty_list_description),
                        StringPresenter(R.string.ok)
                    )
                )
            }
        } else {
            if (isRefreshing) {
                refreshDataEvent.value =
                    Event(
                        LiveDataState.Success(
                            StringPresenter(R.string.data_refreshed_label)
                        )
                    )
            }
           
            statisticsData.value = LiveDataState.Success(
                result.data.sortedByDescending { it.casesModel.total }.map {
                    it.toStatisticsItemData(
                        getCountryCode(it.country),
                        getNewCasesDate(it.casesModel.new),
                        numberFormatProvider
                    )
                }
            )
        }
    }

    private fun getNewCasesDate(totalNewCases: Int?): NewCasesData =
        if (totalNewCases == null || totalNewCases == 0) {
            NewCasesData(
                StringPresenter(R.string.no_new_cases_label),
                null,
                R.color.colorCaseNoNew
            )
        } else {
            NewCasesData(
                StringPresenter(
                    R.string.new_cases_label,
                    numberFormatProvider.format(totalNewCases)
                ),
                R.drawable.ic_new_case_label_icon,
                R.color.colorCaseActive
            )
        }

    private fun getCountryCode(countryName: String): String? =
        fetchCountryCodeUseCase.getCountryCode(countryName)

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            else -> LiveDataState.Error(
                ErrorData(
                    ErrorDataType.RECOVERABLE,
                    StringPresenter(R.string.error_fetch_data_title),
                    StringPresenter(R.string.error_fetch_data_description)
                )
            )
        }
}