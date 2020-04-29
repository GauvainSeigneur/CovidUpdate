package com.gauvain.seigneur.covidupdate.view.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.gauvain.seigneur.covidupdate.R
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        viewModel.historyData.observe(this, Observer {
           Toast.makeText(this, "lol $it", Toast.LENGTH_LONG).show()
        })
    }
}
