package com.gauvain.seigneur.presentation.mocks


import com.gauvain.seigneur.domain.utils.toDate
import com.gauvain.seigneur.presentation.model.CaseStateDistributionItem
import com.gauvain.seigneur.presentation.model.CountryCasesData
import com.gauvain.seigneur.presentation.model.CountryChartHistoryItem
import com.gauvain.seigneur.presentation.model.CountryHistoryData
import com.gauvain.seigneur.domain.model.CountryHistoryItemModel
import com.gauvain.seigneur.domain.model.CountryHistoryModel
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.presentation.R

object CountryHistoryMocks {

    fun getUseCaseCountryHistoryModel(): CountryHistoryModel = CountryHistoryModel(
        country = "France",
        history = listOf(
            dummyCountryHistoryItemModel,
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-02".toDate(SERVER_DATE_FORMAT)
            ),
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-03".toDate(SERVER_DATE_FORMAT)
            ),
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-04".toDate(SERVER_DATE_FORMAT),
                active = 6,
                recovered = 4,
                critical = 4

            ),
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-05".toDate(SERVER_DATE_FORMAT)
            ),
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-06".toDate(SERVER_DATE_FORMAT)
            ),
            dummyCountryHistoryItemModel.copy(
                date = "2020-01-07".toDate(SERVER_DATE_FORMAT)
            )
        )
    )

    fun getCountryHistoryData(): CountryHistoryData = CountryHistoryData(
        casesList = listOf(
            CountryCasesData("6", R.color.colorCaseActive),
            CountryCasesData("2", R.color.colorCaseCritical),
            CountryCasesData("2", R.color.colorCaseRecovered),
            CountryCasesData("0", R.color.colorCaseDead)
        ),
        activeChart = listOf(
            getChartHistoryItemData(
                "2020-01-07",
                8
            ),
            getChartHistoryItemData(
                "2020-01-06",
                8
            ),
            getChartHistoryItemData(
                "2020-01-05",
                8
            ),
            getChartHistoryItemData(
                "2020-01-04",
                6
            ),
            getChartHistoryItemData(
                "2020-01-03",
                8
            ),
            getChartHistoryItemData(
                "2020-01-02",
                8
            ),
            getChartHistoryItemData(
                "2020-01-01",
                8
            )
        ),
        criticalChart = listOf(
            getChartHistoryItemData(
                "2020-01-07",
                2
            ),
            getChartHistoryItemData(
                "2020-01-06",
                2
            ),
            getChartHistoryItemData(
                "2020-01-05",
                2
            ),
            getChartHistoryItemData(
                "2020-01-04",
                4
            ),
            getChartHistoryItemData(
                "2020-01-03",
                2
            ),
            getChartHistoryItemData(
                "2020-01-02",
                2
            ),
            getChartHistoryItemData(
                "2020-01-01",
                2
            )
        ),
        caseDistributionChart = listOf(
            getCaseStateDistributionItem(
                "07/01/2020",
                "07/01/2020",
                0f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "06/01/2020",
                "06/01/2020",
                1f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "05/01/2020",
                "05/01/2020",
                2f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "04/01/2020",
                "04/01/2020",
                3f,
                20.0f,
                40.0f,
                40.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "03/01/2020",
                "03/01/2020",
                4f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "02/01/2020",
                "02/01/2020",
                5f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            ),
            getCaseStateDistributionItem(
                "01/01/2020",
                "01/01/2020",
                6f,
                60.0f,
                20.0f,
                20.0f,
                0.0f
            )
        )
    )

    private fun getChartHistoryItemData(date: String, value: Int): CountryChartHistoryItem =
        CountryChartHistoryItem(
            position = date.toDate(SERVER_DATE_FORMAT).time.toFloat(),
            value = value.toFloat()
        )

    private fun getCaseStateDistributionItem(
        startDate : String,
        endDate:String,
        pos: Float, nonCriticalV: Float, criticalV: Float,
        recoV: Float, deadV: Float
    ):
        CaseStateDistributionItem =
        CaseStateDistributionItem(
            startDate = startDate,
            enDate = endDate,
            position = pos,
            nonCritical = nonCriticalV,
            critical = criticalV,
            recovered = recoV,
            dead = deadV
        )

    private val dummyCountryHistoryItemModel =
        CountryHistoryItemModel(
            date = "2020-01-01".toDate(SERVER_DATE_FORMAT),
            total = 10,
            new = 10,
            active = 8,
            recovered = 2,
            critical = 2,
            dead = 0
        )
}