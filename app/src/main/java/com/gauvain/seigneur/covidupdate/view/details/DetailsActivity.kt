package com.gauvain.seigneur.covidupdate.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.animation.TransitionListenerAdapter
import com.gauvain.seigneur.covidupdate.model.ErrorDataType
import com.gauvain.seigneur.covidupdate.model.base.LiveDataState
import com.gauvain.seigneur.covidupdate.model.LoadingState
import com.gauvain.seigneur.covidupdate.model.SharedTransitionState
import com.gauvain.seigneur.covidupdate.utils.convertDpToPixel
import com.gauvain.seigneur.covidupdate.utils.loadCountry
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.view_details_header_content.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity(), LifecycleObserver {

    companion object {
        private const val FADE_MAX_VALUE = 1f
        private const val COUNTRY_NAME = "country_name"
        private const val COUNTRY_CODE = "country_code"
        fun newIntent(
            context: Context,
            countryName: String,
            countryCode: String?
        ): Intent = Intent(context, DetailsActivity::class.java)
            .putExtra(COUNTRY_NAME, countryName)
            .putExtra(COUNTRY_CODE, countryCode)
    }

    private val appBarOffsetListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val vTotalScrollRange = appBarLayout.totalScrollRange
            val vRatio = (vTotalScrollRange.toFloat() + verticalOffset) / vTotalScrollRange
            detailsHeaderContentView.alpha = (vRatio * FADE_MAX_VALUE)
            //*0.75 because we set the layout_collapseParallaxMultiplier to 0.25 for detailsHeaderContentView
            detailsCountryFlagImageView.y = collapsingCountryPlaceHolder.y + (verticalOffset*0.75f)
            detailsCountryFlagImageView.alpha = (vRatio * FADE_MAX_VALUE)
        }

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.countryName = intent.getStringExtra(COUNTRY_NAME)
        setContentView(R.layout.activity_details)
        loadCountryFlag(intent.getStringExtra(COUNTRY_CODE))
        initViews()
        listenSharedEnterTransition()
    }

    private fun listenSharedEnterTransition() {
        val sharedElementEnterTransition: Transition = window.sharedElementEnterTransition
        sharedElementEnterTransition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionStart(transition: Transition) {
                super.onTransitionStart(transition)
                viewModel.onSharedTransitionStart()
            }

            override fun onTransitionEnd(transition: Transition) {
                super.onTransitionEnd(transition)
                viewModel.onSharedTransitionEnd()
                observeLiveData()
            }
        })
    }

    private fun initViews() {
        bigCoutryNameTextView.text = viewModel.countryName
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
    }

    private fun loadCountryFlag(countryCode: String?) {
        detailsCountryFlagImageView.loadCountry(countryCode)
    }

    override fun onBackPressed() {
        viewModel.onSharedTransitionStart()
        super.onBackPressed()
    }

    private fun observeLiveData() {
        viewModel.sharedTransitionData.observe(this, Observer {
            when (it) {
                SharedTransitionState.STARTED -> {
                    detailsCountryFlagImageView.alpha = 1f
                }
                SharedTransitionState.ENDED -> {
                    activityDetailsBackground.setBackgroundColor(ContextCompat.getColor(
                        this,
                            R.color.colorBackground
                        )
                    )
                }
            }
        })

        viewModel.historyData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    detailsNestedScrollView.visibility = View.VISIBLE
                    activeChart.setData(it.data, "active")
                    distributionChart.setData(it.data.caseDistributionChart, "Distribution")
                }
                is LiveDataState.Error -> {
                    detailsNestedScrollView.visibility = View.INVISIBLE
                    initialLoadingView.visibility = View.VISIBLE
                    initialLoadingView.setError(
                        it.errorData.title.getFormattedString(this@DetailsActivity),
                        it.errorData.description?.getFormattedString(this@DetailsActivity),
                        it.errorData.buttonText?.getFormattedString(this@DetailsActivity)
                    )
                    { manageRetryButtonActions(it.errorData.type) }
                }
            }
        })

        viewModel.loadingData.observe(this, Observer {
            when (it) {
                LoadingState.INITIAL_IS_LOADING -> {
                    detailsNestedScrollView.visibility = View.INVISIBLE
                    initialLoadingView.visibility = View.VISIBLE
                    initialLoadingView.setLoading()
                }
                LoadingState.INITIAL_IS_LOADED -> {
                    initialLoadingView.hide()
                    initialLoadingView.visibility = View.GONE
                }
            }
        })
    }

    private fun manageRetryButtonActions(data: ErrorDataType): Unit =
        when (data) {
            ErrorDataType.RECOVERABLE -> viewModel.getHistory()
            else -> finish()
        }

}
