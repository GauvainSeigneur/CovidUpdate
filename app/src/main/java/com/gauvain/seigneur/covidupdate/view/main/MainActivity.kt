package com.gauvain.seigneur.covidupdate.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.AllHistoryData
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.event.EventObserver
import com.gauvain.seigneur.covidupdate.utils.safeClick.setOnSafeClickListener
import com.gauvain.seigneur.covidupdate.view.BottomMenuDialog
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.CustomSnackbar
import com.github.mikephil.charting.data.Entry
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_chart_view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val FADE_MAX_VALUE = 1f
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
        viewModel.fetchStatistics()
        setContentView(R.layout.activity_main)
        bottomAppBar.setNavigationOnClickListener {
            BottomMenuDialog().show(supportFragmentManager, "BottomMenuDialogTAG")
        }
        refreshFab.setOnSafeClickListener {
            viewModel.refreshStatistics()
        }
        initReviewsListAdapter()
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
        observe()
    }

    private fun observe() {
        viewModel.historyData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    setUpAllData(it.data)
                }
                is LiveDataState.Error -> {
                }
            }
        })

        viewModel.statisticsData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    statisticsListAdapter.updateStatList(it.data)
                }
                is LiveDataState.Error -> {
                }
            }
        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                RequestState.INITIAL_IS_LOADING -> {
                    refreshFab.isClickable = false
                }
                RequestState.INITIAL_IS_LOADED -> {
                    refreshFab.isClickable = true
                }
                RequestState.REFRESH_IS_LOADING -> {
                    refreshFab.isClickable = false
                    showFabLoader(true)
                }
                RequestState.REFRESH_IS_LOADED -> {
                    refreshFab.isClickable = true
                    showFabLoader(false)
                }
            }
        })

        viewModel.refreshDataEvent.observe(this, EventObserver {
            CustomSnackbar.make(
                mainActivityParentLayout, it.getFormattedString(this),
                CustomSnackbar.LENGTH_LONG
            )
                .setAnchorView(refreshFab)
                .show()
        })
    }

    private fun initReviewsListAdapter() {
        statsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        statsRecyclerView.adapter = statisticsListAdapter
    }

    private fun setUpAllData(data: AllHistoryData) {
        val entries = mutableListOf<Entry>()
        data.history.map { item ->
            entries.add(Entry(item.position, item.total))
        }
        allHistoryChartView.setData(entries, "all history")
        allTotalCaseTextview.text = data.totalCases
        allActiveCasesTextView.text = data.activeCases.getFormattedString(this)
        allNewCasesTextView.text = data.newCases.total.getFormattedString(this)
        allNewCasesTextView.setTextColor(ContextCompat.getColor(this, data.newCases.colorRes))
    }

    private fun seeDetails(position: Int) {
    }

    private fun manageHeaderAspect(vRatio: Float) {
        headerChartView.alpha = (vRatio * FADE_MAX_VALUE)
        val scale = vRatio + (SCALE_MAX_VALUE - SCALE_MAX_VALUE * vRatio)
        allHistoryChartView.scaleX = scale
        allHistoryChartView.scaleY = scale
    }

    private fun showFabLoader(isVisible: Boolean) {
        if (isVisible) {
            fabLoadingView.visibility = View.VISIBLE
        } else {
            fabLoadingView.visibility = View.GONE
        }
        refreshFab.shrink(isVisible)
        fabLoadingView.showLoader(isVisible)
        refreshFab.isClickable = !isVisible
    }
}
