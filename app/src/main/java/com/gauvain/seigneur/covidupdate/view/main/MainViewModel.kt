package com.gauvain.seigneur.covidupdate.view.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.AllHistoryModel
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.formatTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

typealias StatisticsState = LiveDataState<List<StatisticsItemData>>
typealias AllHistoryState = LiveDataState<AllHistoryData>

class MainViewModel(
    private val fetchStatisticsUseCase: FetchStatisticsUseCase,
    private val countryCodeProvider: CountryCodeProvider,
    private val fetchAllHistoryUseCase: FetchAllHistoryUseCase
) : ViewModel() {

    companion object {
        const val ALL_COUNTRY_NAME = "all"
        const val WORLD_COUNTRY_NAME = "world"
    }

    val historyData: MutableLiveData<AllHistoryState> by lazy {
        fetchHistory()
        MutableLiveData<AllHistoryState>()
    }
    val loadingState: MutableLiveData<RequestState> = MutableLiveData()
    val statisLiveItemData: MutableLiveData<StatisticsState> by lazy {
        fetchStatistics()
        MutableLiveData<StatisticsState>()
    }

    private fun fetchHistory() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                fetchAllHistoryUseCase.invoke()
            }
            when (result) {
                is Outcome.Success -> {
                    historyData.value = LiveDataState.Success(setUpAllHistoryData(result.data))
                }
                is Outcome.Error -> {
                    Log.d("allhistory", "error ${result.error}")
                }
            }
        }
    }

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
                        statisLiveItemData.value = LiveDataState.Error(
                            ErrorData(
                                null,
                                StringPresenter(R.string.empty_list_title),
                                StringPresenter(R.string.empty_list_description)
                            )
                        )
                    } else {
                        val ascendingList = result.data.sortedByDescending { it.casesModel.total }
                        statisLiveItemData.value = LiveDataState.Success(
                            ascendingList.map {
                                it.toStatisticsItemData(getCountryCode(it.country))
                            }
                        )
                    }
                }
                is Outcome.Error -> {
                    statisLiveItemData.value = setErrorLiveData(result.error)
                }
            }
        }
    }

    private fun setUpAllHistoryData(model: AllHistoryModel): AllHistoryData =
        AllHistoryData(
            totalCases = model.totalCases.toString(),
            totalNewCases = model.totalNewCases.toString(),
            history = setUpAllHistoryList(model)
        )

    private fun setUpAllHistoryList(model: AllHistoryModel): List<AllHistoryItemData> {
        val dataList = mutableListOf<AllHistoryItemData>()
        for ((index, value) in model.history.reversed().withIndex()) {
            dataList.add(
                value.toData(index.toFloat())
            )
        }

        return dataList
    }

    private fun getCountryCode(countryName: String): String? =
        countryCodeProvider.getCountryCode(countryName)

    private fun setErrorLiveData(errorType: ErrorType): LiveDataState.Error =
        when (errorType) {
            else -> LiveDataState.Error(ErrorData())
        }
}