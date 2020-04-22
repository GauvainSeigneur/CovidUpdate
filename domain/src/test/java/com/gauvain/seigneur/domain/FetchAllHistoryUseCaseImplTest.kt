package com.gauvain.seigneur.domain

import com.gauvain.seigneur.domain.mock.OutComeModelMock
import com.gauvain.seigneur.domain.mock.ProviderModelMock
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.gauvain.seigneur.domain.provider.HistoryProvider
import com.gauvain.seigneur.domain.usecase.FetchAllHistoryUseCase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import org.junit.Before
import org.junit.Test
import org.mockito.*

class FetchAllHistoryUseCaseImplTest {

    @Mock
    private lateinit var provider: HistoryProvider
    private lateinit var useCase: FetchAllHistoryUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive success from provider adapter must return OutCome Success`() {
        given(provider.history("all")).willReturn(ProviderModelMock.createAllLongHistoryModel())
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.CreateShortHistoryModel()))
    }

    @Test
    fun `When we receive ERROR UNKNOWN from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.ERROR_UNKNOWN)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }

    @Test
    fun `When we receive ERROR BODY NULL from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.BODY_NULL)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }

    @Test
    fun `When we receive UNAUTHORIZED from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.UNAUTHORIZED)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
    }

    @Test
    fun `When we receive CONNECTION_LOST from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.CONNECTION_LOST)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
    }

    @Test
    fun `When we receive UNKNOWN_HOST from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.UNKNOWN_HOST)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
    }

    @Test
    fun `When we receive SERVER_INTERNAL_ERROR from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.SERVER_INTERNAL_ERROR)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
    }

    @Test
    fun `When we receive UNKNOWN_OBJECT from provider adapter must return OutCome Error`() {
        given(provider.history("all")).willThrow(
            GetHistoryException(RequestExceptionType.UNKNOWN_OBJECT)
        )
        useCase = FetchAllHistoryUseCase.create(provider)
        val outcome = useCase.invoke()
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }
}