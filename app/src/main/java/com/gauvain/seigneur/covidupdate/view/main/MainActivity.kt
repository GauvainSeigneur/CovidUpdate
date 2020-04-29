package com.gauvain.seigneur.covidupdate.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.AllHistoryData
import com.gauvain.seigneur.covidupdate.model.ErrorData
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.AVDUtils
import com.gauvain.seigneur.covidupdate.utils.RequestState
import com.gauvain.seigneur.covidupdate.utils.StringPresenter
import com.gauvain.seigneur.covidupdate.utils.event.EventObserver
import com.gauvain.seigneur.covidupdate.utils.safeClick.setOnSafeClickListener
import com.gauvain.seigneur.covidupdate.utils.startVectorAnimation
import com.gauvain.seigneur.covidupdate.view.BottomMenuDialog
import com.gauvain.seigneur.covidupdate.view.details.DetailsActivity
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.CustomSnackbar
import com.gauvain.seigneur.domain.model.StatisticsItemModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_chart_view.*
import kotlinx.android.synthetic.main.view_content_main.*
import kotlinx.android.synthetic.main.view_no_data_found.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val FADE_MAX_VALUE = 1f
        const val SCALE_MAX_VALUE = 1.5f
        const val BOTTOM_MENU_TAG = "BottomMenuDialogTAG"
        const val STATE_LOADING = "StateLoading"
        const val STATE_ERROR = "StateError"
        const val STATE_GONE = "StateGone"
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
        fetchData()
        setContentView(R.layout.activity_main)
        initView()
        observe()
    }

    private fun fetchData() {
        if (viewModel.statisticsData.value == null ||
            viewModel.statisticsData.value is LiveDataState.Error
        ) {
            viewModel.fetchData()
        }
    }

    private fun initView() {
        bottomAppBar.setNavigationOnClickListener {
            BottomMenuDialog().show(supportFragmentManager, BOTTOM_MENU_TAG)
        }
        refreshFab.setOnSafeClickListener {
            viewModel.refreshData()
        }
        initReviewsListAdapter()
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
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
                    handleLoadingView(STATE_GONE)
                    statisticsListAdapter.updateStatList(it.data)
                }
                is LiveDataState.Error -> {
                    handleLoadingView(STATE_ERROR, it.errorData)
                }
            }
        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                RequestState.INITIAL_IS_LOADING -> handleLoadingView(STATE_LOADING)
                RequestState.REFRESH_IS_LOADING -> handleRefreshLoading(true)
                RequestState.REFRESH_IS_LOADED -> handleRefreshLoading(false)
            }
        })

        viewModel.refreshDataEvent.observe(this, EventObserver {
            displaySnackbar(it)
        })

        viewModel.displayDetailsEvent.observe(this, EventObserver {
            when (it) {
                is LiveDataState.Success -> displayDetails(it.data)
                is LiveDataState.Error -> displaySnackbar(it.errorData.title)
            }
        })
    }

    private fun displayDetails(statisticsItemModel: StatisticsItemModel) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    private fun displaySnackbar(stringPresenter: StringPresenter) {
        CustomSnackbar.make(
            mainActivityParentLayout, stringPresenter.getFormattedString(this),
            CustomSnackbar.LENGTH_LONG
        )
            .setAction(R.string.ok, View.OnClickListener {
            })
            .setAnchorView(refreshFab)
            .show()
    }

    private fun initReviewsListAdapter() {
        statsRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        statsRecyclerView.adapter = statisticsListAdapter
    }

    private fun setUpAllData(data: AllHistoryData) {
        allHistoryChartView.setData(data.chart, getString(R.string.main_header_chart_legend))
        toolbar.title = data.totalCases
        toolbar.subtitle = data.activeCases.getFormattedString(this)
        allTotalCaseTextview.text = data.totalCases
        allActiveCasesTextView.text = data.activeCases.getFormattedString(this)
        allNewCasesTextView.text = data.newCases.total.getFormattedString(this)
        allNewCasesTextView.setTextColor(ContextCompat.getColor(this, data.newCases.colorRes))
    }

    private fun seeDetails(position: Int) {
        viewModel.getItemDetails(position)
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

    private fun handleLoadingView(state: String, errorData: ErrorData? = null) {
        when (state) {
            STATE_LOADING -> {
                refreshFab.hide()
                loadingView.visibility = View.VISIBLE
                bigLoader.visibility = View.VISIBLE
                AVDUtils.startLoadingAnimation(bigLoader, true)
                errorView.visibility = View.GONE
            }
            STATE_ERROR -> {
                refreshFab.hide()
                loadingView.visibility = View.VISIBLE
                bigLoader.visibility = View.GONE
                AVDUtils.startLoadingAnimation(bigLoader, false)
                errorView.visibility = View.VISIBLE
                binocularAvdView.startVectorAnimation()
                errorTitle.text = errorData?.title?.getFormattedString(this)
                errorDesc.text = errorData?.description?.getFormattedString(this)
                retryButton.setOnClickListener {
                    viewModel.fetchData()
                }
            }
            else -> {
                refreshFab.show()
                AVDUtils.startLoadingAnimation(bigLoader, false)
                loadingView.visibility = View.GONE
                bigLoader.visibility = View.GONE
                errorView.visibility = View.GONE
            }
        }
    }

    private fun handleRefreshLoading(isVisible: Boolean) {
        refreshFab.isClickable = !isVisible
        showFabLoader(isVisible)
    }
}
