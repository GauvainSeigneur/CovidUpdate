package com.gauvain.seigneur.data_adapter.mocks

import com.gauvain.seigneur.data_adapter.model.Statistics
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object ResponseGsonObjectMock {

    fun createStatisticsResponse(): Statistics {
        val listType = object : TypeToken<Statistics>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                    "get": "statistics",
                    "parameters": [],
                    "errors": [],
                    "results": 2,
                    "response": 
                    [
                        {
                            "country": "China",
                            "cases": {
                                "new": "+62",
                                "active": 1190,
                                "critical": 189,
                                "recovered": 77279,
                                "total": 81802
                            },
                            "deaths": {
                                "new": "+2",
                                "total": 3333
                            },
                            "tests": {
                                "total": null
                            },
                            "day": "2020-04-08",
                            "time": "2020-04-08T10:15:05+00:00"
                        },
                        {
                            "country": "Italy",
                            "cases": {
                                "new": "+3039",
                                "active": 94067,
                                "critical": 3792,
                                "recovered": 24392,
                                "total": 135586
                            },
                            "deaths": {
                                "new": "+604",
                                "total": 17127
                            },
                            "tests": {
                                "total": 755445
                            },
                            "day": "2020-04-08",
                            "time": "2020-04-08T10:15:05+00:00"
                        }
                    ]
                }
                    """.trimIndent(), listType
        )
    }

    fun createHistoryResponse(): Statistics {
        val listType = object : TypeToken<Statistics>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                    "get": "history",
                    "parameters": {
                        "country": "france"
                    },
                    "errors": [],
                    "results": 2,
                    "response": [
                        {
                            "country": "France",
                            "cases": {
                                "new": "+11059",
                                "active": 79404,
                                "critical": 7131,
                                "recovered": 19337,
                                "total": 109069
                            },
                            "deaths": {
                                "new": "+1417",
                                "total": 10328
                            },
                            "tests": {
                                "total": 224254
                            },
                            "day": "2020-04-08",
                            "time": "2020-04-08T09:45:05+00:00"
                        },
                        {
                            "country": "France",
                            "cases": {
                                "new": "+1847",
                                "active": 12310,
                                "critical": 1525,
                                "recovered": 1587,
                                "total": 14459
                            },
                            "deaths": {
                                "new": "+112",
                                "total": 562
                            },
                            "tests": {
                                "total": null
                            },
                            "day": "2020-03-22",
                            "time": "2020-03-22T03:15:05+00:00"
                        }
                    ]
                }
                    """.trimIndent(), listType
        )
    }

    fun createMessageResponse(): Statistics {
        val wantedObject = object : TypeToken<Statistics>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                   "message": "Invalid X-Rapidapi-Key"
                }
                    """.trimIndent(), wantedObject
        )
    }

    fun createNullBodyResponse(): Statistics {
        val wantedObject = object : TypeToken<Statistics>() {
        }.type
        return GsonBuilder().create().fromJson(
            """
                {
                }
                    """.trimIndent(), wantedObject
        )
    }
}