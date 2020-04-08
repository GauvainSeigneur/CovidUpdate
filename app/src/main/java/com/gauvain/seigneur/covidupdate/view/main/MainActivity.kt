package com.gauvain.seigneur.covidupdate.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val statsViewModel: MainViewModel by viewModel()
    private val statisticsListAdapter by lazy {
        StatisticsListAdapter { position -> seeDetails(position) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initReviewsListAdapter()
        observe()
    }

    private fun observe() {
        statsViewModel.historyData.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    val entries = mutableListOf<Entry>()
                    it.data.history.map {item ->
                        entries.add(Entry(item.position, item.total))
                    }
                    allHistoryChartView.setData(entries, "all history")
                }
                is LiveDataState.Error -> {

                }
            }


        })
        statsViewModel.statistics.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    statisticsListAdapter.updateStatList(it.data)
                    val entries = mutableListOf<Entry>()
                    entries.add(Entry(0f, 10f))
                    entries.add(Entry(5f, 20f))
                    entries.add(Entry(10f, 10f))
                    allHistoryChartView.setData(entries, "all history")
                }
                is LiveDataState.Error -> {

                }
            }


        })

        statsViewModel.loadingState.observe(this, Observer {
            when (it) {
                RequestState.IS_LOADING -> {
                }
                RequestState.IS_LOADED -> {
                }
            }
        })
    }

    private fun initReviewsListAdapter() {
        statsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        statsRecyclerView.adapter = statisticsListAdapter
    }

    private fun seeDetails(position: Int) {
    }
}
