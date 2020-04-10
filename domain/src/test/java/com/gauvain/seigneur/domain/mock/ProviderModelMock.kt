package com.gauvain.seigneur.domain.mock

import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.DeathsModel
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.SERVER_TIME_FORMAT
import com.gauvain.seigneur.domain.utils.toDate

object ProviderModelMock {

    fun createStatisticsModelListWorldAndAll():List<StatisticsItemModel> =
        listOf(
            StatisticsItemModel(
                country = "All",
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
                country = "world",
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

    fun createAllLongHistoryModel(): List<StatisticsItemModel> =
        listOf<StatisticsItemModel>(
            StatisticsItemModel(
                country = "All",
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
                country = "All",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:00+00:00".toDate(SERVER_TIME_FORMAT),
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
                country = "All",
                day = "2020-04-07".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:00+00:00".toDate(SERVER_TIME_FORMAT),
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
}