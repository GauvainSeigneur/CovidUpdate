package com.gauvain.seigneur.covidupdate.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.animation.makeSceneTransitionAnimation
import com.gauvain.seigneur.presentation.model.AllHistoryData
import com.gauvain.seigneur.presentation.model.ErrorData
import com.gauvain.seigneur.presentation.model.base.LiveDataState
import com.gauvain.seigneur.presentation.model.LoadingState
import com.gauvain.seigneur.presentation.utils.event.EventObserver
import com.gauvain.seigneur.covidupdate.utils.safeClick.setOnSafeClickListener
import com.gauvain.seigneur.covidupdate.view.BottomMenuDialog
import com.gauvain.seigneur.covidupdate.view.details.DetailsActivity
import com.gauvain.seigneur.covidupdate.widget.customSnackbar.CustomSnackbar
import com.gauvain.seigneur.presentation.MainViewModel
import com.gauvain.seigneur.presentation.utils.StringPresenter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.country_stat_list.*
import kotlinx.android.synthetic.main.header_chart_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(), StatisticsListAdapter.Listener {

    companion object {
        const val DELAY_FILTER = 100L
        const val FADE_MAX_VALUE = 1f
        const val SCALE_MAX_VALUE = 1.5f
        const val BOTTOM_MENU_TAG = "BottomMenuDialogTAG"
        const val STATE_LOADING = "StateLoading"
        const val STATE_ERROR = "StateError"
        const val STATE_GONE = "StateGone"

        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    private val viewModel: MainViewModel by viewModel()
    private val statisticsListAdapter by lazy {
        StatisticsListAdapter(this)
    }
    private val appBarOffsetListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val vTotalScrollRange = appBarLayout.totalScrollRange
            val vRatio = (vTotalScrollRange.toFloat() + verticalOffset) / vTotalScrollRange
            manageHeaderAspect(vRatio)
        }
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        observe()
    }

    override fun onClick(
        countryName: String,
        countryCode: String?,
        rootView: View,
        flagImageView: View,
        totalCases: StringPresenter
    ) {
        val options = makeSceneTransitionAnimation(
            this@MainActivity,
            Pair(rootView, getString(R.string.transition_root)),
            Pair(flagImageView, getString(R.string.transition_country_flag))
        )
        startActivity(
            DetailsActivity.newIntent(
                this, countryName,
                totalCases.getFormattedString(this),
                countryCode
            ),
            options
                .toBundle()
        )
    }

    private fun initView() {
        bottomAppBar.setNavigationOnClickListener {
            BottomMenuDialog().show(supportFragmentManager, BOTTOM_MENU_TAG)
        }
        refreshFab.setOnSafeClickListener {
            viewModel.refresh()
        }
        initReviewsListAdapter()
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
        searchInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // user is typing: reset already started timer (if existing)
                timer?.cancel()
            }
            override fun afterTextChanged(s: Editable?) { applyFilter(s) }
        })
    }

    private fun observe() {
        viewModel.historyData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    setUpAllData(it.data)
                }
                is LiveDataState.Error -> {
                    //must display a dedicated view
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

        viewModel.loadingData.observe(this, Observer {
            when (it) {
                LoadingState.INITIAL_IS_LOADING -> handleLoadingView(STATE_LOADING)
                LoadingState.REFRESH_IS_LOADING -> handleRefreshLoading(true)
                LoadingState.REFRESH_IS_LOADED -> handleRefreshLoading(false)
            }
        })

        viewModel.refreshDataEvent.observe(this, EventObserver {
            when (it) {
                is LiveDataState.Success -> displaySnackbar(it.data)
                is LiveDataState.Error -> displaySnackbar(it.errorData.title)
            }
        })
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
                initialLoadingView.visibility = View.VISIBLE
                initialLoadingView.setLoading()
            }
            STATE_ERROR -> {
                refreshFab.hide()
                initialLoadingView.setError(
                    errorData?.title?.getFormattedString(this),
                    errorData?.description?.getFormattedString(this),
                    errorData?.buttonText?.getFormattedString(this)
                ) { viewModel.retry() }
            }
            else -> {
                refreshFab.show()
                initialLoadingView.hide()
                initialLoadingView.visibility = View.GONE
            }
        }
    }

    private fun handleRefreshLoading(isVisible: Boolean) {
        refreshFab.isClickable = !isVisible
        showFabLoader(isVisible)
    }

    private fun applyFilter(s: Editable?) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                this@MainActivity.runOnUiThread(Runnable {
                    statisticsListAdapter.filter.filter(s)
                })
            }
        }, DELAY_FILTER)
    }
}
