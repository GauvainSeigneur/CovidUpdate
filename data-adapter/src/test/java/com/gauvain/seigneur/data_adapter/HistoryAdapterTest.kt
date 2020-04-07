package com.gauvain.seigneur.data_adapter

import com.gauvain.seigneur.data_adapter.adapters.HistoryAdapter
import com.gauvain.seigneur.data_adapter.mocks.CovidUpdateServiceMock
import com.gauvain.seigneur.data_adapter.model.RequestExceptionContent
import com.gauvain.seigneur.data_adapter.service.CovidService
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_ERROR_UNKNOWN_DESC
import com.gauvain.seigneur.data_adapter.utils.EXCEPTION_UNKNOWN_HOST_DESC
import com.gauvain.seigneur.domain.model.RequestExceptionType
import com.gauvain.seigneur.domain.provider.GetHistoryException
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.StringSpec
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.net.UnknownHostException

class historyAdapterTest : StringSpec({

    """Given the StatisticsProvider
        When we get the statistics
        And there is an UNKNOWN HOST ERROR with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownHostException())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching { provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(RequestExceptionType.UNKNOWN_HOST)
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(EXCEPTION_UNKNOWN_HOST_DESC)
    }

    """Given the StatisticsProvider
        When we get the statistics
        And there is an UNKNOWN ERROR with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(UnknownError())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching { provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(RequestExceptionType.ERROR_UNKNOWN)
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(EXCEPTION_ERROR_UNKNOWN_DESC)
    }

    """Given the StatisticsProvider
        When we get the statistics
        And there is an EXCEPTION with the web service
        Then it should throw an GetHistoryException""" {
        val serviceFailedResponse: CovidService =
            CovidUpdateServiceMock.createServiceThatFail(Exception())
        val provider = HistoryAdapter(serviceFailedResponse)
        val result = runCatching { provider.history("country")
        }
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(GetHistoryException::class.java)
        assertThat((result.exceptionOrNull() as GetHistoryException).type).isEqualTo(RequestExceptionType.ERROR_UNKNOWN)
        assertThat((result.exceptionOrNull() as GetHistoryException).description).isEqualTo(EXCEPTION_ERROR_UNKNOWN_DESC)
    }


})