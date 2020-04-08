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
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import org.mockito.*

class FetchAllHistoryUseCaseImplTest : StringSpec() {

    @Mock
    private lateinit var provider: HistoryProvider
    private lateinit var useCase: FetchAllHistoryUseCase

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
            given(provider.history("all")).willReturn(ProviderModelMock.createAllLongHistoryModel())
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Success(OutComeModelMock.CreateShortHistoryModel()))
        }

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider throw a GetHistory(ErrorUnknown)
         Then we should return an OutCome.Error
         """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.ERROR_UNKNOWN)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider throw a GetHistory(BodyNull)
         Then we should return an OutCome.Error
         """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.BODY_NULL)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider throw a GetHistory(UNAUTHORIZED)
         Then we should return an OutCome.Error
         """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.UNAUTHORIZED)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNAUTHORIZED))
        }

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider throw a GetHistory(CONNECTION_LOST)
         Then we should return an OutCome.Error
         """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.CONNECTION_LOST)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_CONNECTION_LOST))
        }

        """
         Given the we fetch history data for all country
         When the request is made
         And the provider throw a GetHistory(UNKNOWN_HOST)
         Then we should return an OutCome.Error
         """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.UNKNOWN_HOST)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN_HOST))
        }

        """
        Given the we fetch history data for all country
        When the request is made
        And the provider throw a GetHistory(SERVER_INTERNAL_ERROR)
        Then we should return an OutCome.Error
        """ {
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.SERVER_INTERNAL_ERROR)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_INTERNAL_SERVER))
        }

        """
        Given the we fetch history data for all country
        When the request is made
        And the provider throw a GetHistory(UNKNOWN_OBJECT)
        Then we should return an OutCome.Error
        """{
            given(provider.history("all")).willThrow(
                GetHistoryException(RequestExceptionType.UNKNOWN_OBJECT)
            )
            useCase = FetchAllHistoryUseCase.create(provider)
            val outcome = useCase.invoke()
            assertThat(outcome).isNotNull()
            assertThat(outcome).isEqualTo(Outcome.Error(ErrorType.ERROR_UNKNOWN))
        }

    }

}