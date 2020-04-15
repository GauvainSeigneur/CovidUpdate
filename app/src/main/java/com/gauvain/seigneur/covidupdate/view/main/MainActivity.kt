package com.gauvain.seigneur.covidupdate.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.AllHistoryData
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.github.mikephil.charting.data.Entry
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val FADE_MIN_VALUE = 0.7f
        const val SCALE_MAX_VALUE = 1.5f
    }

    private val viewModel: MainViewModel by viewModel()
    private val statisticsListAdapter by lazy {
        StatisticsListAdapter { position -> seeDetails(position) }
    }

    private val appBarOffsetListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val vTotalScrollRange = appBarLayout.totalScrollRange
            val vRatio = (vTotalScrollRange.toFloat() + verticalOffset) / vTotalScrollRange
            manageHeaderAspect(vRatio)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refreshFab.setOnClickListener {
            viewModel.refreshStatistics()
        }
        initReviewsListAdapter()
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
        observe()
    }

    private fun observe() {
        viewModel.historyData.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    setUpAllData(it.data)
                }
                is LiveDataState.Error -> {
                }
            }

        })

        viewModel.statistics.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    statisticsListAdapter.updateStatList(it.data)
                }
                is LiveDataState.Error -> {

                }
            }

        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                RequestState.IS_LOADING -> {
                    refreshFab.isClickable = false
                }
                RequestState.IS_LOADED -> {
                    refreshFab.isClickable = true
                }
            }
        })
    }

    private fun initReviewsListAdapter() {
        statsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        statsRecyclerView.adapter = statisticsListAdapter
    }

    private fun setUpAllData(data: AllHistoryData) {
        val entries = mutableListOf<Entry>()
        data.history.map {item ->
            entries.add(Entry(item.position, item.total))
        }
        allHistoryChartView.setData(entries, "all history")
        allTotalCaseTextview.text = data.totalCases
        allNewActiveCasesTextView.text = data.newActiveCases.getFormattedString(this)
    }

    private fun seeDetails(position: Int) {
    }

    private fun manageHeaderAspect(vRatio: Float) {
        allHistoryChartView.alpha = (vRatio * FADE_MIN_VALUE)
        val scale = vRatio + (SCALE_MAX_VALUE - SCALE_MAX_VALUE * vRatio)
        allHistoryChartView.scaleX = scale
        allHistoryChartView.scaleY = scale
    }
}
