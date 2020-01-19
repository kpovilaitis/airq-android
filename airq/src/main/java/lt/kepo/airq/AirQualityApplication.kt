package lt.kepo.airq

import android.app.Application
import lt.kepo.airquality.di.airQualityModule
import lt.kepo.core.di.coreModule
import lt.kepo.stations.di.stationsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AirQualityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AirQualityApplication)
            modules(
                listOf(
                    coreModule,
                    airQualityModule,
                    stationsModule
                )
            )
        }
    }
}