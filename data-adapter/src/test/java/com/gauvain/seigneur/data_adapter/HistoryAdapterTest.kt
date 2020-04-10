package com.gauvain.seigneur.data_adapter

import com.gauvain.seigneur.data_adapter.adapters.HistoryAdapter
import com.gauvain.seigneur.data_adapter.mocks.AdapterOutcomeModelMock
import com.gauvain.seigneur.data_adapter.mocks.CovidUpdateServiceMock
import com.gauvain.seigneur.data_adapter.mocks.ResponseGsonObjectMock
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_BODY_NUL_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_CONNECTION_LOST_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_ERROR_UNKNOWN_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_UNKNOWN_HOST_DESC
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.StringSpec
import java.io.IOException
import java.lang.Exception
import java.net.UnknownHostException

class historyAdapterTest : StringSpec({

    """Given the HistoryProvider
        When we get the history
        And we receive the object from the web service
        Then it should return list of statistics""" {
        val serviceSuccessResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createHistoryResponse())
        val provider = HistoryAdapter(serviceSuccessResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.isSuccess).isNotNull()
        assertThat(result.getOrNull()).isEqualTo(AdapterOutcomeModelMock.createHistoryStatisticsListSuccess())
    }

    """Given the HistoryProvider
        When we get the history
        And we receive an object from the web service which include only a message string
        Then it should throw an GetHistoryException""" {
        val messageResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createMessageResponse())
        val provider = HistoryAdapter(messageResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.UNAUTHORIZED
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            "Invalid X-Rapidapi-Key")
    }

    """Given the HistoryProvider
        When we get the history
        And we receive a null object
        Then it should throw an GetHistoryException""" {
        val messageResponse: CovidService =
            CovidUpdateServiceMock.createServiceWithResponses(ResponseGsonObjectMock.createNullBodyResponse())
        val provider = HistoryAdapter(messageResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.BODY_NULL
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            EXCEPTION_BODY_NUL_DESC)
    }

    """Given the HistoryProvider
        When we get the history
        And there is an UNKNOWN HOST ERROR with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownHostException())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.UNKNOWN_HOST
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            EXCEPTION_UNKNOWN_HOST_DESC
        )
    }

    """Given the HistoryProvider
        When we get the history
        And there is an UNKNOWN ERROR with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownError())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.ERROR_UNKNOWN
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            EXCEPTION_ERROR_UNKNOWN_DESC
        )
    }

    """Given the HistoryProvider
        When we get the history
        And there is an IOException with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(IOException())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.CONNECTION_LOST
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            EXCEPTION_CONNECTION_LOST_DESC
        )
    }

    """Given the HistoryProvider
        When we get the history
        And there is an EXCEPTION with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(Exception())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching {
            provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(
            RequestExceptionType.ERROR_UNKNOWN
        )
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(
            EXCEPTION_ERROR_UNKNOWN_DESC
        )
    }
})