package com.gauvain.seigneur.domain

import com.gauvain.seigneur.domain.mock.OutComeModelMock
import com.gauvain.seigneur.domain.mock.ProviderModelMock
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryHistoryUseCaseImpl
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.*

class FetchCountryHistoryUseCaseImplTest {

    @Mock
    private lateinit var provider: HistoryProvider
    @InjectMocks
    private lateinit var useCase: FetchCountryHistoryUseCaseImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive success from provider adapter must return OutCome Success`() {
        runBlockingTest {
            given(provider.history("france")).willReturn(ProviderModelMock.createCountryLongHistoryModel())
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.createCountryHistoryModel()))
        }
    }

    @Test
    fun `When we receive ERROR UNKNOWN from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.ERROR_UNKNOWN)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }
    }

    @Test
    fun `When we receive ERROR VALUE NULL from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.VALUE_NULL)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }
    }

    @Test
    fun `When we receive UNAUTHORIZED from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.UNAUTHORIZED)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
        }
    }

    @Test
    fun `When we receive CONNECTION_LOST from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.CONNECTION_LOST)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
        }
    }

    @Test
    fun `When we receive UNKNOWN_HOST from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.UNKNOWN_HOST)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
        }
    }

    @Test
    fun `When we receive SERVER_INTERNAL_ERROR from provider adapter must return OutCome Error`() {
        runBlockingTest {
            given(provider.history("france")).willThrow(
                GetHistoryException(RequestExceptionType.SERVER_INTERNAL_ERROR)
            )
            val outcome = useCase.invoke("france")
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
        }
    }

}