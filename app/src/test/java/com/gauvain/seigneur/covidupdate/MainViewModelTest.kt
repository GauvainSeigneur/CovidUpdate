package com.gauvain.seigneur.covidupdate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gauvain.seigneur.covidupdate.mocks.AllHistoryDataMock
import com.gauvain.seigneur.covidupdate.mocks.HistoryModelMocks
import com.gauvain.seigneur.covidupdate.mocks.StatisticsDataMocks
import com.gauvain.seigneur.covidupdate.mocks.StatisticsModelMocks
import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.*
import com.gauvain.seigneur.covidupdate.utils.event.Event
import com.gauvain.seigneur.covidupdate.view.main.MainViewModel
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    // Run tasks synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    // Sets the main coroutines dispatcher to a TestCoroutineScope for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        given(fetchCountryCodeUseCase.getCountryCode("France")).willReturn("FR")
        given(fetchCountryCodeUseCase.getCountryCode("USA")).willReturn("US")
        given(fetchCountryCodeUseCase.getCountryCode("Spain")).willReturn("ES")
        given(fetchCountryCodeUseCase.getCountryCode("Netherlands")).willReturn("NL")
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun
        given_statistics_usecase_return_list_when_fetch_statistics_then_liveData_must_return_data_list() {
        given(statisticsUseCase.invoke(null)).willReturn(
            Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList())
        )
        viewModel.fetchData()
        mainCoroutineRule.advanceUntilIdle()
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
        viewModel.fetchData()
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
        viewModel.statisticsData.value = LiveDataState.Success(
            StatisticsDataMocks.getStatisticsList(numberFormatProvider)
        )
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(listOf()))
        viewModel.fetchData()
        val event = viewModel.refreshDataEvent.getOrAwaitValue {
            mainCoroutineRule.advanceUntilIdle()
        }
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
        given_statistics_livedata_is_success_when_fetch_statistics_with_error_then_liveData_must_return_event_error() {
        viewModel.statisticsData.value = LiveDataState.Success(
            StatisticsDataMocks.getStatisticsList(numberFormatProvider)
        )
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        viewModel.fetchData()
        mainCoroutineRule.advanceUntilIdle()
        val event = viewModel.refreshDataEvent.getOrAwaitValue()
        assertEquals(
            event,
            Event(
                LiveDataState.Error(
                    ErrorData(
                        ErrorDataType.INFORMATIVE,
                        StringPresenter(
                            R.string.error_refresh_data_label
                        )
                    )
                )
            )
        )
    }

    @Test
    fun
        given_statistics_livedata_is_success_when_fetch_statistics_with_success_again_then_liveData_must_return_event_success() {
        viewModel.statisticsData.value = LiveDataState.Success(
            StatisticsDataMocks.getStatisticsList(numberFormatProvider)
        )
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(StatisticsModelMocks.getStatisticsItemModelList()))
        viewModel.fetchData()
        val event = viewModel.refreshDataEvent.getOrAwaitValue {
            // After observing, advance the clock to avoid the delay calls.
            mainCoroutineRule.advanceUntilIdle()
        }
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
        viewModel.fetchData()
        val value = viewModel.historyData.getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Success(
                AllHistoryDataMock.getAllHistoryDataMock(numberFormatProvider)
            )
        )
    }
}