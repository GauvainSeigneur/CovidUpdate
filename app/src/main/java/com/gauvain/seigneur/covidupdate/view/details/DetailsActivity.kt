package com.gauvain.seigneur.covidupdate.view.details

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import coil.request.Request
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.animation.TransitionListenerAdapter
import com.gauvain.seigneur.covidupdate.model.CountryCasesData
import com.gauvain.seigneur.covidupdate.model.ErrorDataType
import com.gauvain.seigneur.covidupdate.model.LoadingState
import com.gauvain.seigneur.covidupdate.model.SharedTransitionState
import com.gauvain.seigneur.covidupdate.model.base.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.loadCountry
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.view_details_header_content.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity(), LifecycleObserver {

    companion object {
        private const val FADE_MAX_VALUE = 1f
        private const val START_TRANSITION_DELAY = 125L // half transition delay
        private const val COUNTRY_NAME = "country_name"
        private const val COUNTRY_CODE = "country_code"
        private const val TOTAL_CASES = "total_cases"
        fun newIntent(
            context: Context,
            countryName: String,
            totalCases: String,
            countryCode: String?
        ): Intent = Intent(context, DetailsActivity::class.java)
            .putExtra(COUNTRY_NAME, countryName)
            .putExtra(TOTAL_CASES, totalCases)
            .putExtra(COUNTRY_CODE, countryCode)
    }

    private val appBarOffsetListener: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val vTotalScrollRange = appBarLayout.totalScrollRange
            val vRatio = (vTotalScrollRange.toFloat() + verticalOffset) / vTotalScrollRange
            detailsHeaderContentView.alpha = (vRatio * FADE_MAX_VALUE)
            //*0.75 because we set the layout_collapseParallaxMultiplier to 0.25 for detailsHeaderContentView
            detailsCountryFlagImageView.y = collapsingCountryPlaceHolder.y + (verticalOffset * 0.75f)
            detailsCountryFlagImageView.alpha = (vRatio * FADE_MAX_VALUE)
        }
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.countryName = intent.getStringExtra(COUNTRY_NAME)
        setContentView(R.layout.activity_details)
        // Postpone the shared element enter transition.
        postponeEnterTransition()
        initViews()
        loadCountryFlag(intent.getStringExtra(COUNTRY_CODE))
        listenSharedEnterTransition()
    }

    private fun initViews() {
        bigCountryNameTextView.text = viewModel.countryName
        bigTotalCasesTextView.text = intent.getStringExtra(TOTAL_CASES)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        appBarLayout.addOnOffsetChangedListener(appBarOffsetListener)
    }

    private fun loadCountryFlag(countryCode: String?) {
        detailsCountryFlagImageView.loadCountry(countryCode, object : Request.Listener {
            override fun onStart(request: Request) {
                super.onStart(request)
                //avoid image blink due to the loading of the
                Handler().postDelayed({
                    startPostponedEnterTransition()
                }, START_TRANSITION_DELAY)
            }
        })
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
                    activityDetailsBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorBackground
                        )
                    )
                }
                else -> {
                }
            }
        })

        viewModel.historyData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    detailsNestedScrollView.visibility = View.VISIBLE
                    activeCriticalEvolutionBlock.setData(it.data, null)
                    casesDistributionBlock.setData(it.data.caseDistributionChart, null)
                    displayCasesChips(it.data.casesList)
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

    private fun displayCasesChips(countryCases: List<CountryCasesData>) {
        for (item in countryCases) {
            val chip = Chip(this@DetailsActivity)
            chip.text = item.value
            chip.chipIcon = ContextCompat.getDrawable(this@DetailsActivity, R.drawable.ic_circle)
            chip.chipIconTint = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@DetailsActivity,
                    item.color
                )
            )
            casesChipGroup.addView(chip)
        }
    }

    private fun manageRetryButtonActions(data: ErrorDataType): Unit =
        when (data) {
            ErrorDataType.RECOVERABLE -> viewModel.getHistory()
            else -> finish()
        }
}
