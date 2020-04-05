package com.gauvain.seigneur.covidupdate.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.data.LiveDataState
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
    }

    private fun observe() {
        statsViewModel.statisLiveItemData.observe(this, Observer {
            when(it) {
                is LiveDataState.Success -> {
                    statisticsListAdapter.updateStatList(it.data)
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
        statsRecyclerView.itemAnimator = null
        // Removes blinks
        (statsRecyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        statsRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
                ContextCompat.getDrawable(this@MainActivity, R.drawable.list_divider)?.let {
                    setDrawable(it)
                }
            }
        )
    }

    private fun seeDetails(position: Int) {
    }
}
