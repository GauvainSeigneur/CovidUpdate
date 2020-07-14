package com.gauvain.seigneur.presentation

import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.presentation.utils.event.Event
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.gauvain.seigneur.presentation.mocks.AllHistoryDataMock
import com.gauvain.seigneur.presentation.mocks.HistoryModelMocks
import com.gauvain.seigneur.presentation.mocks.StatisticsDataMocks
import com.gauvain.seigneur.presentation.mocks.StatisticsModelMocks
import com.gauvain.seigneur.presentation.utils.BaseViewModelTest
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var statisticsUseCase: FetchStatisticsUseCase
    @Mock
    private lateinit var historyUseCase: FetchAllHistoryUseCase
    @Mock
    private lateinit var fetchCountryCodeUseCase: FetchCountryCodeUseCase
    @Mock
    private lateinit var numberFormatProvider: NumberFormatProvider
    @InjectMocks
    private lateinit var viewModel: MainViewModel

    override fun setup() {
        MockitoAnnotations.initMocks(this)
        given(fetchCountryCodeUseCase.getCountryCode("France")).willReturn("FR")
        given(fetchCountryCodeUseCase.getCountryCode("USA")).willReturn("US")
        given(fetchCountryCodeUseCase.getCountryCode("Spain")).willReturn("ES")
        given(fetchCountryCodeUseCase.getCountryCode("Netherlands")).willReturn("NL")
        super.setup()
    }

    @Test
    fun given_statistics_usecase_return_list_when_fetch_statistics_then_liveData_must_return_data_list() {
        given(statisticsUseCase.invoke(null)).willReturn(
            Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList())
        )
        val value = viewModel.statisticsData.getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Success(
                StatisticsDataMocks.getStatisticsList(numberFormatProvider)
            )
        )
    }

    @Test
    fun given_statistics_usecase_return_empty_list_when_fetch_statistics_then_liveData_must_return_empty_error() {
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(listOf()))
        val result = viewModel.statisticsData.getOrAwaitValue()
        assertEquals(
            result, LiveDataState.Error(
                ErrorData(
                    ErrorDataType.NOT_RECOVERABLE,
                    StringPresenter(R.string.empty_list_title),
                    StringPresenter(R.string.empty_list_description),
                    StringPresenter(R.string.ok)
                )
            )
        )
    }

    @Test
    fun
        given_statistics_livedata_is_success_when_fetch_statistics_return_empty_list_when_fetch_statistics_then_dedicated_event_must_be_set() {
        //first call is a success
        given(statisticsUseCase.invoke(null)).willReturn(
            Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList())
        )
        //viewModel.fetchData()
        viewModel.statisticsData.getOrAwaitValue()
        //second call is a fail - check that a dedicated event is send and the liveData don't
        // change
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(listOf()))
        viewModel.refresh()
        val event = viewModel.refreshDataEvent.getOrAwaitValue()
        assertEquals(
            event,
            Event(
                LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.INFORMATIVE,
                        StringPresenter(R.string.error_refresh_data_label)
                    )
                )
            )
        )
    }

    @Test
    fun
        given_statistics_livedata_is_success_when_fetch_statistics_with_success_again_then_liveData_must_return_event_success() {
        //first call is a success
        given(statisticsUseCase.invoke(null)).willReturn(
            Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList())
        )
        viewModel.statisticsData.getOrAwaitValue()
        //second call is a success again
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList()))
        viewModel.refresh()
        val event = viewModel.refreshDataEvent.getOrAwaitValue()
        assertEquals(
            event,
            Event(LiveDataState.Success(StringPresenter(R.string.data_refreshed_label)))
        )
    }

    @Test
    fun
        given_hsitory_usecase_return_list_when_fetch_history_then_liveData_must_return_data_list() {
        given(numberFormatProvider.format(10000)).willReturn("10000")
        given(historyUseCase.invoke()).willReturn(
            Outcome.Success(HistoryModelMocks.getHistoryModelMock())
        )
        val value = viewModel.historyData.getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Success(
                AllHistoryDataMock.getAllHistoryDataMock(numberFormatProvider)
            )
        )
    }
}