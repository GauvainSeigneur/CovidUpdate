package com.gauvain.seigneur.data_adapter.mocks

import com.gauvain.seigneur.data_adapter.model.Stat
import com.gauvain.seigneur.data_adapter.model.Statistics
import com.gauvain.seigneur.domain.model.CasesModel
import com.gauvain.seigneur.domain.model.DeathsModel
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.gauvain.seigneur.domain.utils.DATA_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.SERVER_DATE_FORMAT
import com.gauvain.seigneur.domain.utils.SERVER_TIME_FORMAT
import com.gauvain.seigneur.domain.utils.toDate
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object AdapterOutcomeModelMock {

    fun createStatisticsListSuccess(): List<StatisticsItemModel> =
        listOf<StatisticsItemModel>(
            StatisticsItemModel(
                country = "China",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T10:15:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 62,
                    active = 1190,
                    critical = 189,
                    recovered = 77279,
                    total = 81802
                ),
                deathsModel = DeathsModel(
                    new = "+2",
                    total = 3333
                )
            ),
            StatisticsItemModel(
                country = "Italy",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T10:15:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 3039,
                    active = 94067,
                    critical = 3792,
                    recovered = 24392,
                    total = 135586
                ),
                deathsModel = DeathsModel(
                    new = "+604",
                    total = 17127
                )
            )
        )

    fun createHistoryStatisticsListSuccess(): List<StatisticsItemModel> =
        listOf<StatisticsItemModel>(
            StatisticsItemModel(
                country = "France",
                day = "2020-04-08".toDate(SERVER_DATE_FORMAT),
                time = "2020-04-08T09:45:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 11059,
                    active = 79404,
                    critical = 7131,
                    recovered = 19337,
                    total = 109069
                ),
                deathsModel = DeathsModel(
                    new = "+1417",
                    total = 10328
                )
            ),
            StatisticsItemModel(
                country = "France",
                day = "2020-03-22".toDate(SERVER_DATE_FORMAT),
                time = "2020-03-22T03:15:05+00:00".toDate(SERVER_TIME_FORMAT),
                casesModel = CasesModel(
                    new = 1847,
                    active = 12310,
                    critical = 1525,
                    recovered = 1587,
                    total = 14459
                ),
                deathsModel = DeathsModel(
                    new = "+112",
                    total = 562
                )
            )
        )
}