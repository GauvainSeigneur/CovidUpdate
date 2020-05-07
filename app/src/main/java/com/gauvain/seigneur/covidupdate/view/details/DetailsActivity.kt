package com.gauvain.seigneur.covidupdate.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.animation.TransitionListenerAdapter
import com.gauvain.seigneur.covidupdate.model.ErrorDataType
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.LoadingState
import com.gauvain.seigneur.covidupdate.utils.SharedTransitionState
import com.gauvain.seigneur.covidupdate.utils.loadCountry
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity(), LifecycleObserver {

    companion object {

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

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.countryName = intent.getStringExtra(COUNTRY_NAME)
        val countryCode = intent.getStringExtra(COUNTRY_CODE)
        setContentView(R.layout.activity_details)
        detailsCountryFlagImageView.loadCountry(countryCode)
        collapsingCountryFlagImageView.loadCountry(countryCode)
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
    }

    override fun onBackPressed() {
        viewModel.onSharedTransitionStart()
        super.onBackPressed()
    }

    private fun observeLiveData() {
        viewModel.sharedTransitionData.observe(this, Observer {
            when (it) {
                SharedTransitionState.STARTED -> {
                    collapsingCountryFlagImageView.visibility = View.GONE
                    detailsCountryFlagImageView.visibility = View.VISIBLE
                }
                SharedTransitionState.ENDED -> {
                    activityDetailsBackground.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorBackground
                        )
                    )
                    collapsingCountryFlagImageView.visibility = View.VISIBLE
                    detailsCountryFlagImageView.visibility = View.GONE
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
