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
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import org.mockito.*

class FetchStatisticsUseCaseImplTest : StringSpec() {

    @Mock
    private lateinit var provider: StatisticsProvider
    private lateinit var useCase: FetchStatisticsUseCase

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        MockitoAnnotations.initMocks(this)
    }

    init {

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider return a success result
         Then we should return an OutCome.Success
         """ {
            given(provider.statistics(null)).willReturn(ProviderModelMock.createStatisticsModelListWorldAndAll())
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.createStatisticsModelListWithoutWorldAndAll()))
        }

        """
         Given the we fetch statistics data
         When the request is made
         And the provider throw a GetHistory(ErrorUnknown)
         Then we should return an OutCome.Error
         """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.ERROR_UNKNOWN)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

        """
         Given the we fetch statistics data
         When the request is made
         And the provider throw a GetHistory(BodyNull)
         Then we should return an OutCome.Error
         """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.BODY_NULL)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

        """
         Given the we fetch statistics data
         When the request is made
         And the provider throw a GetHistory(UNAUTHORIZED)
         Then we should return an OutCome.Error
         """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.UNAUTHORIZED)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
        }

        """
         Given the we fetch statistics datay
         When the request is made
         And the provider throw a GetHistory(CONNECTION_LOST)
         Then we should return an OutCome.Error
         """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.CONNECTION_LOST)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
        }

        """
         Given the we fetch statistics data
         When the request is made
         And the provider throw a GetHistory(UNKNOWN_HOST)
         Then we should return an OutCome.Error
         """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.UNKNOWN_HOST)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
        }

        """
        Given the we fetch statistics data
        When the request is made
        And the provider throw a GetHistory(SERVER_INTERNAL_ERROR)
        Then we should return an OutCome.Error
        """ {
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.SERVER_INTERNAL_ERROR)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
        }

        """
        Given the we fetch statistics data
        When the request is made
        And the provider throw a GetHistory(UNKNOWN_OBJECT)
        Then we should return an OutCome.Error
        """{
            given(provider.statistics()).willThrow(
                GetStatisticsException(RequestExceptionType.UNKNOWN_OBJECT)
            )
            useCase = FetchStatisticsUseCase.create(provider)
            val outcome = useCase.invoke(null)
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

    }

}