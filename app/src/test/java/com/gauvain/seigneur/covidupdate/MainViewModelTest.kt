package com.gauvain.seigneur.covidupdate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.view.main.MainViewModel
import com.gauvain.seigneur.covidupdate.view.main.StatisticsState
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.CountryCodeProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import io.kotest.core.test.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(JUnit4::class)
class MainViewModelTest {

    @Mock
    private lateinit var statisticsUseCase: FetchStatisticsUseCase
    @Mock
    private lateinit var historyUseCase: FetchAllHistoryUseCase
    @Mock
    private lateinit var countryCodeProvider: CountryCodeProvider
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @InjectMocks
    private lateinit var viewModel: MainViewModel
    @Mock
    lateinit var statisticsLiveDataObserver: Observer<StatisticsState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun exampleTest() = runBlockingTest {
        given(statisticsUseCase.invoke(null)).willReturn(Outcome.Success(listOf()))
        viewModel.statistics.observeForever(statisticsLiveDataObserver)
        verify(statisticsLiveDataObserver).onChanged(
            LiveDataState.Success(
                listOf(
                )
            )
        )
    }
}