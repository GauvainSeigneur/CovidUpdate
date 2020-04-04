package com.gauvain.seigneur.covidupdate.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.utils.RequestState
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val statsViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe()
    }

    private fun observe() {
        statsViewModel.statisticsModel.observe(this, Observer{
            Toast.makeText(this@MainActivity, "lol $it", Toast.LENGTH_LONG).show()
        })

        statsViewModel.loadingState.observe(this, Observer {
            when(it) {
                RequestState.IS_LOADING -> { }
                RequestState.IS_IN_ERROR -> {}
                RequestState.IS_EMPTY -> {}
                RequestState.IS_LOADED -> {}
            }
        })
    }
}
