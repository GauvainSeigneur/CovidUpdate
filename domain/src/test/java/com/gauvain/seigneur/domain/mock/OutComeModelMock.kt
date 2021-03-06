package com.gauvain.seigneur.domain.mock

import com.gauvain.seigneur.domain.model.*
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.SERVER_TIME_FORMAT
import com.gauvain.seigneur.domain.utils.toDate

object OutComeModelMock {

    fun createStatisticsModelListWithoutWorldAndAll(): List<StatisticsItemModel> =
        listOf(
            StatisticsItemModel(
                country = "China",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 1000,
                    active = 70000,
                    critical = 1000,
                    recovered = 35000,
                    total = 107000
                ),
                deathsModel = DeathsModel(
                    new = "+200",
                    total = 1200
                )
            ),
            StatisticsItemModel(
                country = "France",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 1000,
                    active = 70000,
                    critical = 1000,
                    recovered = 35000,
                    total = 107000
                ),
                deathsModel = DeathsModel(
                    new = "+200",
                    total = 1200
                )
            ),
            StatisticsItemModel(
                country = "Italy",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 800,
                    active = 60000,
                    critical = 200,
                    recovered = 3500,
                    total = 64500
                ),
                deathsModel = DeathsModel(
                    new = "+100",
                    total = 900
                )
            )
        )

    fun CreateShortHistoryModel(): AllHistoryModel =
        AllHistoryModel(
            totalCases = 107000,
            totalNewCases = 1000,
            totalActiveCases = 70000,
            activeHistory = listOf(
                AllActiveHistoryItemModel(
                    total = 70000,
                    day = "2020-04-08".toDate(SERVER_DATE_FORMAT)
                ),
                AllActiveHistoryItemModel(
                    total = 60000,
                    day = "2020-04-07".toDate(SERVER_DATE_FORMAT)
                )
            )
        )

    fun createCountryHistoryModel(): CountryHistoryModel =
        CountryHistoryModel(
            country = "France",
            history = listOf(
                CountryHistoryItemModel(
                    date = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                    total = 107000,
                    new = 1000,
                    active = 70000,
                    recovered = 35000,
                    critical = 1000,
                    dead = 1200
                ),
                CountryHistoryItemModel(
                    date = "2020-04-07".toDate(SERVER_DATE_FORMAT),
                    total = 64500,
                    new = 800,
                    active = 60000,
                    recovered = 3500,
                    critical = 200,
                    dead = 900
                )
            )
        )
}