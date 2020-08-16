package com.gauvain.seigneur.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gauvain.seigneur.presentation.utils.event.Event
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.ioJob
import kotlinx.coroutines.*

typealias StatisticsState = LiveDataState<List<StatisticsItemData>>
typealias AllHistoryState = LiveDataState<AllHistoryData>
typealias RefreshEventState = Event<LiveDataState<StringPresenter>>

class MainViewModel(
    private val fetchStatisticsUseCase: FetchStatisticsUseCase,
    private val fetchAllHistoryUseCase: FetchAllHistoryUseCase,
    private val fetchCountryCodeUseCase: FetchCountryCodeUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel() {

    companion object {
        const val LONG_DELAY = 400L
        const val SMALL_DELAY = 350L
        const val NO_DELAY = 0L
    }

    //LiveData
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val loadingData :LiveData<LoadingState> = loadingState

    private val historyState: MutableLiveData<AllHistoryState> by lazy {
        ioJob {
            fetchHistory()
        }
        MutableLiveData<AllHistoryState>()
    }
    val historyData: LiveData<AllHistoryState> by lazy {
        historyState
    }

    private val statisticsState: MutableLiveData<StatisticsState> by lazy {
        ioJob {
            fetchStatistics()
        }
        MutableLiveData<StatisticsState>()
    }
    val statisticsData: LiveData<StatisticsState> by lazy {
        statisticsState
    }

    //Event (LiveData which can be consumed only once)
    private val refreshState = MutableLiveData<RefreshEventState>()
    val refreshDataEvent :LiveData<RefreshEventState> = refreshState

    private fun fetchData() {
        ioJob {
            fetchStatistics()
        }
        ioJob {
            fetchHistory()
        }
    }

    fun retry() {
        fetchData()
    }

    fun refresh() {
        loadingState.postValue(LoadingState.REFRESH_IS_LOADING)
        ioJob {
            delay(LONG_DELAY)
            fetchStatistics()
        }
        ioJob {
            delay(LONG_DELAY)
            fetchHistory()
        }
    }

    private suspend fun fetchStatistics() {
        //check if the data is refreshing or get for the first time for UI purpose
        //if it is a refresh, we add a small delay to get a smoother UI for loading animation
        val isRefreshData = statisticsData.value != null && statisticsData.value is LiveDataState.Success
        val resultDelay = when (isRefreshData) {
            true -> {
                SMALL_DELAY
            }
            else -> {
                loadingState.postValue(LoadingState.INITIAL_IS_LOADING)
                NO_DELAY
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

    private fun fetchHistory() {
        when (val result = fetchAllHistoryUseCase.invoke()) {
            is Outcome.Success -> {
                historyState.postValue(
                    LiveDataState.Success(
                        result.data.toData(numberFormatProvider)
                    )
                )
            }
        }
    }

    private fun getStatisticsOutcome(
        isRefreshing: Boolean,
        country: String? = null
    ): Outcome<List<StatisticsItemModel>, ErrorType> {
        val result = fetchStatisticsUseCase.invoke(country)
        when (isRefreshing) {
            true -> loadingState.postValue(LoadingState.REFRESH_IS_LOADED)
            else -> loadingState.postValue(LoadingState.INITIAL_IS_LOADED)
        }
        return result
    }

    private fun handleStatisticsOutcomeError(
        result: Outcome.Error<ErrorType>,
        isRefreshing: Boolean
    ) {
        if (isRefreshing) {
            refreshState.postValue(
                Event(
                    LiveDataState.Error(
                        ErrorData(
                            ErrorDataType.INFORMATIVE, StringPresenter(
                                R.string.error_refresh_data_label
                            )
                        )
                    )
                )
            )
        } else {
            statisticsState.postValue(setErrorLiveData(result.error))
        }
    }

    private fun handleStatisticsOutComeSuccess(
        result: Outcome.Success<List<StatisticsItemModel>>,
        isRefreshing: Boolean
    ) {
        if (result.data.isEmpty()) {
            if (isRefreshing) {
                refreshState.postValue(
                    Event(
                        LiveDataState.Error(
                            ErrorData(
                                ErrorDataType.INFORMATIVE, StringPresenter(
                                    R.string.error_refresh_data_label
                                )
                            )
                        )
                    )
                )
            } else {
                statisticsState.postValue(
                    LiveDataState.Error(
                        ErrorData(
                            ErrorDataType.NOT_RECOVERABLE,
                            StringPresenter(R.string.empty_list_title),
                            StringPresenter(R.string.empty_list_description),
                            StringPresenter(R.string.ok)
                        )
                    )
                )
            }
        } else {
            if (isRefreshing) {
                refreshState.postValue(
                    Event(
                        LiveDataState.Success(
                            StringPresenter(R.string.data_refreshed_label)
                        )
                    )
                )
            }

            statisticsState.postValue(LiveDataState.Success(
                result.data.sortedByDescending { it.casesModel.total }.map {
                    it.toStatisticsItemData(
                        getCountryCode(it.country),
                        getNewCasesDate(it.casesModel.new),
                        numberFormatProvider
                    )
                }
            )
            )
        }
    }

    private fun getNewCasesDate(totalNewCases: Int?): NewCasesData =
        if (totalNewCases == null || totalNewCases == 0) {
            NewCasesData(
                StringPresenter(R.string.no_new_cases_label),

                R.color.colorCaseNoNew
            )
        } else {
            NewCasesData(
                StringPresenter(
                    R.string.new_cases_label,
                    numberFormatProvider.format(totalNewCases)
                ),
                R.color.colorCaseActive,
                R.drawable.ic_new_case_label_icon
            )
        }

    private fun getCountryCode(countryName: String): String? =
        fetchCountryCodeUseCase.getCountryCode(countryName)

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            ErrorType.ERROR_UNAUTHORIZED ->
                LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.NOT_RECOVERABLE,
                        StringPresenter(R.string.error_unauthorized_title),
                        StringPresenter(R.string.error_unauthorized_description)
                    )
                )
            else -> LiveDataState.Error(
                ErrorData(
                    ErrorDataType.RECOVERABLE,
                    StringPresenter(R.string.error_fetch_data_title),
                    StringPresenter(R.string.error_fetch_data_description)
                )
            )
        }
}