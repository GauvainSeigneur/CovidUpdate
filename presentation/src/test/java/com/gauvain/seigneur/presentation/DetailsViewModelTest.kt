package com.gauvain.seigneur.presentation

import com.gauvain.seigneur.presentation.model.ErrorData
import com.gauvain.seigneur.presentation.model.ErrorDataType
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCase
import com.gauvain.seigneur.presentation.mocks.CountryHistoryMocks
import com.gauvain.seigneur.presentation.utils.BaseViewModelTest
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DetailsViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var usecase: FetchCountryHistoryUseCase
    @Mock
    private lateinit var numberFormatProvider: NumberFormatProvider
    @InjectMocks
    private lateinit var viewModel: DetailsViewModel

    override fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel.countryName = "france"
        super.setup()
    }

    @Test
    fun given_usecase_return_valid_data_view_model_must_return_data() {
        given(numberFormatProvider.format(0)).willReturn("0")
        given(numberFormatProvider.format(2)).willReturn("2")
        given(numberFormatProvider.format(8)).willReturn("8")
        given(numberFormatProvider.format(10)).willReturn("10")
        given(numberFormatProvider.format(8-2)).willReturn("6")
        runBlockingTest {
            given(usecase.invoke("france")).willReturn(
                Outcome.Success(CountryHistoryMocks.getUseCaseCountryHistoryModel())
            )
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Success(
                    CountryHistoryMocks.getCountryHistoryData()
                )
            )
        }
    }

    @Test
    fun given_usecase_return_ERROR_UNKNOWN_HOST_view_model_must_return_error() {
       runBlockingTest {
            given(usecase.invoke("france")).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
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

    @Test
    fun given_usecase_return_ERROR_CONNECTION_LOST_view_model_must_return_error() {
       runBlockingTest {
            given(usecase.invoke("france")).willReturn(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
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

    @Test
    fun given_usecase_return_ERROR_UNAUTHORIZED_view_model_must_return_error() {
        runBlockingTest {
            given(usecase.invoke("france")).willReturn(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
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

    @Test
    fun given_usecase_return_ERROR_INTERNAL_SERVER_view_model_must_return_error() {
        runBlockingTest {
            given(usecase.invoke("france")).willReturn(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
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

    @Test
    fun given_usecase_return_UNKNNOWN_ERROR_view_model_must_return_error() {
       runBlockingTest {
            given(usecase.invoke("france")).willReturn(Outcome.Error(ErrorType.ERROR_UNKNOWN))
            viewModel.getHistory()
            val value = viewModel.historyData.getOrAwaitValue()
            assertEquals(
                value, LiveDataState.Error(
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

    @Test
    fun given_country_name_is_null_view_model_must_return_error() {
        viewModel.countryName = null
        viewModel.getHistory()
        val value = viewModel.historyData.getOrAwaitValue()
        assertEquals(
            value, LiveDataState.Error(
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