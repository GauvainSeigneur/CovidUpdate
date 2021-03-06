package com.gauvain.seigneur.presentation.mocks

import com.gauvain.seigneur.presentation.model.*
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.presentation.R
import com.gauvain.seigneur.presentation.utils.QuantityStringPresenter
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.gauvain.seigneur.presentation.utils.addDay
import java.util.*

object StatisticsDataMocks {

    fun getStatisticsList(
        numberFormatProvider: NumberFormatProvider
    ):
        List<StatisticsItemData> =
        listOf(
            StatisticsItemData(
                "USA",  CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.new_cases_label,
                            numberFormatProvider.format(10000)
                        ),
                        R.color.colorCaseActive,
                        R.drawable.ic_new_case_label_icon
                    ),
                    QuantityStringPresenter(
                        R.plurals.active_case_label,
                        20000,
                        numberFormatProvider.format(20000)
                    ),
                    StringPresenter(
                        R.string.total_case_label,
                        numberFormatProvider.format(30000)
                    )
                ),
                "US"
            ),
            StatisticsItemData(
                "Spain",  CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.no_new_cases_label
                        ),
                        R.color.colorCaseNoNew
                    ),
                    QuantityStringPresenter(
                        R.plurals.active_case_label,
                        2000,
                        numberFormatProvider.format(2000)
                    ),
                    StringPresenter(
                        R.string.total_case_label,
                        numberFormatProvider.format(2500)
                    )
                ),
                "ES"
            ),
            StatisticsItemData(
                "France",  CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.new_cases_label,
                            numberFormatProvider.format(100)
                        ),
                        R.color.colorCaseActive,
                        R.drawable.ic_new_case_label_icon
                    ),
                    QuantityStringPresenter(
                        R.plurals.active_case_label,
                        200,
                        numberFormatProvider.format(200)
                    ),
                    StringPresenter(
                        R.string.total_case_label,
                        numberFormatProvider.format(300)
                    )
                ),
                "FR"
            ),
            StatisticsItemData(
                "Netherlands", CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.no_new_cases_label
                        ),
                        R.color.colorCaseNoNew
                    ),
                    QuantityStringPresenter(
                        R.plurals.active_case_label,
                        0,
                        numberFormatProvider.format(0)
                    ),
                    StringPresenter(
                        R.string.total_case_label,
                        numberFormatProvider.format(0)
                    )
                ),
                "NL"
            )
        )
}

object StatisticsModelMocks {

    fun getStatisticsItemModelList(): List<StatisticsItemModel> =
        listOf(
            StatisticsItemModel(
                "France",
                CasesModel(100, 200, 50, 100, 300),
                DeathsModel("+1", 2),
                Date(),
                Date()
            ),
            StatisticsItemModel(
                "USA",
                CasesModel(10000, 20000, 5000, 10000, 30000),
                DeathsModel("+100", 200),
                Date(),
                Date()
            ),
            StatisticsItemModel(
                "Spain",
                CasesModel(0, 2000, 500, 1000, 2500),
                DeathsModel("+10", 20),
                Date(),
                Date()
            ),
            StatisticsItemModel(
                "Netherlands",
                CasesModel(null, 0, 0, 0, 0),
                DeathsModel(null, 0),
                Date(),
                Date()
            )
        )
}

object AllHistoryDataMock {
    fun getAllHistoryDataMock(numberFormatProvider: NumberFormatProvider): AllHistoryData =
        AllHistoryData(
            totalCases = numberFormatProvider.format(10000),
            activeCases = QuantityStringPresenter(
                R.plurals.main_header_chart_subtitle_active,
                8000, numberFormatProvider.format(8000)
            ),
            newCases = AllHistoryNewCaseData(
                StringPresenter(
                    R.string.main_header_chart_subtitle_new,
                    numberFormatProvider.format(1000)
                ),
                R.color.colorCaseActive
            ),
            chart = listOf(
                ChartAllHistoryItem(
                    Date().addDay(2).time.toFloat(),
                    400.toFloat()
                ),
                ChartAllHistoryItem(
                    Date().addDay(1).time.toFloat(),
                    200.toFloat()
                ),
                ChartAllHistoryItem(
                    Date().time.toFloat(),
                    100.toFloat()
                )
            )
        )
}

object HistoryModelMocks {

    fun getHistoryModelMock(): AllHistoryModel =
        AllHistoryModel(
            totalCases = 10000,
            totalNewCases = 1000,
            totalActiveCases = 8000,
            activeHistory = listOf(
                AllActiveHistoryItemModel(
                    100,
                    Date()
                ),
                AllActiveHistoryItemModel(
                    200,
                    Date().addDay(1)
                ),
                AllActiveHistoryItemModel(
                    400,
                    Date().addDay(2)
                )
            )
        )
}