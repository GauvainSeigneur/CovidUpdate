package com.gauvain.seigneur.data_adapter

import com.gauvain.seigneur.data_adapter.adapters.StatisticsAdapter
import com.gauvain.seigneur.data_adapter.mocks.AdapterOutcomeModelMock
import com.gauvain.seigneur.data_adapter.mocks.CovidUpdateServiceMock
import com.gauvain.seigneur.data_adapter.mocks.ResponseGsonObjectMock
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_BODY_NUL_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_CONNECTION_LOST_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_ERROR_UNKNOWN_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_UNKNOWN_HOST_DESC
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetStatisticsException
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.lang.Exception
import java.net.UnknownHostException

class StatisticsAdapterTest {

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When we receive object from provider it must return a list of statistics`() {
        val serviceSuccessResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createStatisticsResponse())
        val provider = StatisticsAdapter(serviceSuccessResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.isSuccess).isNotNull()
        assertThat(result.getOrNull()).isEqualTo(AdapterOutcomeModelMock.createStatisticsListSuccess())
    }

    @Test
    fun `When we receive only a message from provider it must return GetStatisticsException`() {
        val messageResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createMessageResponse())
        val provider = StatisticsAdapter(messageResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.UNAUTHORIZED
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            "Invalid X-Rapidapi-Key")
    }

    @Test
    fun `When we receive null object from provider it must return GetStatisticsException`() {
        val messageResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createNullBodyResponse())
        val provider = StatisticsAdapter(messageResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.BODY_NULL
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            EXCEPTION_BODY_NUL_DESC)
    }

    @Test
    fun `When we receive UNKNOWN HOST ERROR from provider it must return GetStatisticsException`() {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownHostException())
        val provider = StatisticsAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.UNKNOWN_HOST
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            EXCEPTION_UNKNOWN_HOST_DESC
        )
    }

    @Test
    fun `When we receive UNKNOWN ERROR from provider it must return GetStatisticsException`() {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownError())
        val provider = StatisticsAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.ERROR_UNKNOWN
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            EXCEPTION_ERROR_UNKNOWN_DESC
        )
    }

    @Test
    fun `When we receive IOException from provider it must return GetStatisticsException`() {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(IOException())
        val provider = StatisticsAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.CONNECTION_LOST
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            EXCEPTION_CONNECTION_LOST_DESC
        )
    }

    @Test
    fun `When we receive EXCEPTION from provider it must return GetStatisticsException`() {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(Exception())
        val provider = StatisticsAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.statistics()
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetStatisticsException::class.java)
        assertThat((result.exceptionOrNull() as GetStatisticsException).type).isEqualTo(
            RequestExceptionType.ERROR_UNKNOWN
        )
        assertThat((result.exceptionOrNull() as GetStatisticsException).description).isEqualTo(
            EXCEPTION_ERROR_UNKNOWN_DESC
        )
    }
}