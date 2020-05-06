package com.gauvain.seigneur.covidupdate.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.ErrorDataType
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.LoadingState
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    companion object {

        const val COUNTRY_NAME = "country_name"
        fun newIntent(
            context: Context,
            countryName: String
        ): Intent = Intent(context, DetailsActivity::class.java)
            .putExtra(COUNTRY_NAME, countryName)
    }

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.countryName = intent.getStringExtra(COUNTRY_NAME)
        setContentView(R.layout.activity_details)
        initViews()
        observe()
    }

    private fun initViews() {
        toolbar.title = viewModel.countryName
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observe() {
        viewModel.historyData.observe(this, Observer {
            when (it) {
                is LiveDataState.Success -> {
                    activeChart.setData(it.data, "active")
                    distributionChart.setData(it.data.caseDistributionChart, "Distribution")
                }
                is LiveDataState.Error -> {
                    initialLoadingView.setError(
                        it.errorData.title.getFormattedString(this@DetailsActivity),
                        it.errorData.description?.getFormattedString(this@DetailsActivity),
                        it.errorData.buttonText?.getFormattedString(this@DetailsActivity))
                    { manageRetryButtonActions(it.errorData.type) }
                }
            }
        })

        viewModel.loadingData.observe(this, Observer {
            when (it) {
                LoadingState.INITIAL_IS_LOADING -> initialLoadingView.setLoading()
                LoadingState.INITIAL_IS_LOADED -> initialLoadingView.hide()
            }
        })
    }

    private fun manageRetryButtonActions(data: ErrorDataType): Unit =
        when (data) {
            ErrorDataType.RECOVERABLE -> viewModel.getHistory()
            else -> finish()
        }
}
