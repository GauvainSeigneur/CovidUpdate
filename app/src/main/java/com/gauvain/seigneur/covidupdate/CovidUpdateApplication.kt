package com.gauvain.seigneur.covidupdate

import android.app.Application
import com.gauvain.seigneur.covidupdate.injection.useCaseModule
import com.gauvain.seigneur.covidupdate.injection.viewModelModule
import com.gauvain.seigneur.data_adapter.injection.adapterModule
import com.gauvain.seigneur.data_adapter.injection.remoteDataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CovidUpdateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CovidUpdateApplication)
            androidFileProperties()
            modules(
                listOf(
                    remoteDataSourceModule,
                    adapterModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }

    }
}