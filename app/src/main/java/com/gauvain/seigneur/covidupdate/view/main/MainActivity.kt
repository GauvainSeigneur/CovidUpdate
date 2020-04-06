package com.gauvain.seigneur.covidupdate.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.model.LiveDataState
import com.gauvain.seigneur.covidupdate.utils.RequestState
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
        headerStatView.setData()
    }

    private fun observe() {
        statsViewModel.statisLiveItemData.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    Toast.makeText(this, "total ${it.data.totalData.totalCases} new ${it.data
                        .totalData.totalNewCases} list ${it.data.mostImpactCountriesData}" , Toast
                        .LENGTH_LONG).show()
                    statisticsListAdapter.updateStatList(it.data.stats)
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
