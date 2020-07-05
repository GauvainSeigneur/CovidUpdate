package com.gauvain.seigneur.covidupdate

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.gauvain.seigneur.presentation.injection.useCaseModule
import com.gauvain.seigneur.presentation.injection.viewModelModule
import com.gauvain.seigneur.presentation.injection.adapterModule
import com.gauvain.seigneur.presentation.injection.remoteDataSourceModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CovidUpdateApplication : Application(), ImageLoaderFactory {

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

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this@CovidUpdateApplication).apply {
            componentRegistry {
                add(SvgDecoder(this@CovidUpdateApplication))
            }
        }
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this@CovidUpdateApplication))
                    .build()
            }
            .build()
    }
}