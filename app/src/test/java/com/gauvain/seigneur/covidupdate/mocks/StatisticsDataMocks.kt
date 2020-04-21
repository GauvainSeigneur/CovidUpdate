package com.gauvain.seigneur.covidupdate.mocks

import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.*
import com.gauvain.seigneur.covidupdate.utils.QuantityStringPresenter
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.provider.NumberFormatProvider
import com.gauvain.seigneur.domain.usecase.FetchCountryCodeUseCase
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.addDay
import com.gauvain.seigneur.domain.utils.formatTo
import com.github.mikephil.charting.data.Entry
import java.util.*

object StatisticsDataMocks {

    fun getStatisticsList(
        numberFormatProvider: NumberFormatProvider
    ):
        List<StatisticsItemData> =
        listOf(
            StatisticsItemData(
                "USA", "US", CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.new_cases_label,
                            numberFormatProvider.format(10000)
                        ),
                        R.drawable.ic_new_case_label_icon,
                        R.color.colorDanger
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
                )
            ),
            StatisticsItemData(
                "Spain", "ES", CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.no_new_cases_label
                        ),
                        null,
                        R.color.colorCool
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
                )
            ),
            StatisticsItemData(
                "France", "FR", CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.new_cases_label,
                            numberFormatProvider.format(100)
                        ),
                        R.drawable.ic_new_case_label_icon,
                        R.color.colorDanger
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
                )
            ),
            StatisticsItemData(
                "Netherlands", "NL", CasesData(
                    NewCasesData(
                        StringPresenter(
                            R.string.no_new_cases_label
                        ),
                        null,
                        R.color.colorCool
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
                )
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
                R.color.colorDanger
            ),
            history = listOf(
                AllHistoryItemData(100, Date().formatTo(DATA_DATE_FORMAT)),
                AllHistoryItemData(200, Date().addDay(1).formatTo(DATA_DATE_FORMAT)),
                AllHistoryItemData(400, Date().addDay(2).formatTo(DATA_DATE_FORMAT))
            ),
            chart = listOf(
                Entry(
                    Date().addDay(2).time.toFloat(),
                    400.toFloat()
                ),
                Entry(
                    Date().addDay(1).time.toFloat(),
                    200.toFloat()
                ),
                Entry(
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
            history = listOf(
                AllHistoryItemModel(
                    100,
                    Date()
                ),
                AllHistoryItemModel(
                    200,
                    Date().addDay(1)
                ),
                AllHistoryItemModel(
                    400,
                    Date().addDay(2)
                )
            )
        )
}