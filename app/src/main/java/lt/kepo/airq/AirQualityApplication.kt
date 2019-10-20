package lt.kepo.airq

import android.app.Application
import lt.kepo.airq.di.airQualityModule
import lt.kepo.airq.di.appModule
import lt.kepo.airq.di.stationsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class AirQualityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AirQualityApplication)
            modules(
                listOf(
                    appModule,
                    airQualityModule,
                    stationsModule
                )
            )
        }
    }
}