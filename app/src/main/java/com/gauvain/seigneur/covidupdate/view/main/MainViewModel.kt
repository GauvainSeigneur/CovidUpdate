package com.gauvain.seigneur.covidupdate.view.main

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.covidupdate.utils.event.Event
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias StatisticsState = LiveDataState<List<StatisticsItemData>>
typealias AllHistoryState = LiveDataState<AllHistoryData>

class MainViewModel(
    private val fetchStatisticsUseCase: FetchStatisticsUseCase,
    private val countryCodeProvider: CountryCodeProvider,
    private val fetchAllHistoryUseCase: FetchAllHistoryUseCase,
    private val numberFormatProvider: NumberFormatProvider
) : ViewModel() {

    companion object {
        const val LONG_DELAY = 400L
        const val SMALL_DELAY = 350L
        const val NO_DELAY = 0L
    }

    val historyData: MutableLiveData<AllHistoryState> = MutableLiveData()
    val loadingState: MutableLiveData<RequestState> = MutableLiveData()
    val statisticsData = MutableLiveData<StatisticsState>()
    val refreshDataEvent = MutableLiveData<Event<StringPresenter>>()

    fun fetchStatistics() {
        viewModelScope.launch {
            val isRefresh = statisticsData.value != null && statisticsData.value is LiveDataState.Success
            val delay: Long
            when (isRefresh) {
                true -> {
                    delay = SMALL_DELAY
                }
                else -> {
                    loadingState.value = RequestState.INITIAL_IS_LOADING
                    delay = NO_DELAY
                }
            }
            val result = getStatisticsOutcome(isRefresh)
            Handler().postDelayed({
                when (result) {
                    is Outcome.Success -> {
                        handleStatisticsOutComeSuccess(result, isRefresh)
                    }
                    is Outcome.Error -> {
                        handleStatisticsOutcomeError(result, isRefresh)
                    }
                }
            }, delay)
        }
        fetchHistory()
    }

    fun refreshStatistics() {
        loadingState.value = RequestState.REFRESH_IS_LOADING
        Handler().postDelayed({
            fetchStatistics()
            fetchHistory()
        }, LONG_DELAY)
    }

    private fun fetchHistory() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                fetchAllHistoryUseCase.invoke()
            }
            when (result) {
                is Outcome.Success -> {
                    historyData.value = LiveDataState.Success(
                        result.data.toData(numberFormatProvider)
                    )
                }
                is Outcome.Error -> {
                }
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
            true -> loadingState.value = RequestState.REFRESH_IS_LOADED
            else -> loadingState.value = RequestState.INITIAL_IS_LOADED
        }
        return result
    }

    private fun handleStatisticsOutcomeError(
        result: Outcome.Error<ErrorType>,
        isRefreshing: Boolean
    ) {
        if (isRefreshing) {
            refreshDataEvent.value = Event(StringPresenter(R.string.error_refresh_data_label))
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
            } else {
                statisticsData.value = LiveDataState.Error(
                    ErrorData(
                        StringPresenter(R.string.empty_list_title),
                        StringPresenter(R.string.empty_list_description)
                    )
                )
            }
        } else {
            if (isRefreshing) {
                refreshDataEvent.value = Event(
                    StringPresenter(
                        R.string.data_refreshed_label
                    )
                )
            }
            val ascendingList = result.data.sortedByDescending { it.casesModel.total }
            statisticsData.value = LiveDataState.Success(
                ascendingList.map {
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
                R.color.colorCool
            )
        } else {
            NewCasesData(
                StringPresenter(
                    R.string.new_cases_label,
                    numberFormatProvider.format(totalNewCases)
                ),
                R.drawable.ic_new_case_label_icon,
                R.color.colorDanger
            )
        }

    private fun getCountryCode(countryName: String): String? =
        countryCodeProvider.getCountryCode(countryName)

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            else -> LiveDataState.Error(
                ErrorData(
                    StringPresenter(R.string.error_fetch_data_title),
                    StringPresenter(R.string.error_fetch_data_description)
                )
            )
        }
}