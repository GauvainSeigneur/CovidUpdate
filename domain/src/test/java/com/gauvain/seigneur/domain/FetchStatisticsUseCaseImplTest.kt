package com.gauvain.seigneur.domain

import com.gauvain.seigneur.domain.mock.OutComeModelMock
import com.gauvain.seigneur.domain.mock.ProviderModelMock
import com.gauvain.seigneur.domain.model.ErrorType
import com.gauvain.seigneur.domain.model.Outcome
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.gauvain.seigneur.domain.provider.StatisticsProvider
import com.gauvain.seigneur.domain.usecase.FetchStatisticsUseCase
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import org.junit.Before
import org.junit.Test
import org.mockito.*

class FetchStatisticsUseCaseImplTest {

    @Mock
    private lateinit var provider: StatisticsProvider
    private lateinit var useCase: FetchStatisticsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive success from provider adapter must return OutCome Success`() {
        given(provider.statistics(null)).willReturn(ProviderModelMock.createStatisticsModelListWorldAndAll())
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.createStatisticsModelListWithoutWorldAndAll()))
    }

    @Test
    fun `When we receive ERROR UNKNOWN from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.ERROR_UNKNOWN)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }

    @Test
    fun `When we receive ERROR BODY NULL from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.BODY_NULL)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }

    @Test
    fun `When we receive UNAUTHORIZED from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.UNAUTHORIZED)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
    }

    @Test
    fun `When we receive CONNECTION_LOST from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.CONNECTION_LOST)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
    }

    @Test
    fun `When we receive UNKNOWN_HOST from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.UNKNOWN_HOST)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
    }

    @Test
    fun `When we receive SERVER_INTERNAL_ERROR from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.SERVER_INTERNAL_ERROR)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
    }

    @Test
    fun `When we receive UNKNOWN_OBJECT from provider adapter must return OutCome Error`() {
        given(provider.statistics()).willThrow(
            GetStatisticsException(RequestExceptionType.UNKNOWN_OBJECT)
        )
        useCase = FetchStatisticsUseCase.create(provider)
        val outcome = useCase.invoke(null)
        assertThat(outcome).isNotNull()
        assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
    }
}