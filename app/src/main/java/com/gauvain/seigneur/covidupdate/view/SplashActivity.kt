package com.gauvain.seigneur.covidupdate.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gauvain.seigneur.covidupdate.R
import com.gauvain.seigneur.covidupdate.view.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity(MainActivity.newIntent(this))
        finish()
    }
}
