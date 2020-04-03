package com.gauvain.seigneur.covidupdate

import android.app.Application
import com.gauvain.seigneur.data_adapter.remoteDataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CovidUpdateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CovidUpdateApplication)
            androidFileProperties()
            modules(listOf(remoteDataSourceModule))
        }
    }
}