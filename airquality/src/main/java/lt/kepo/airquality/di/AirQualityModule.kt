package lt.kepo.airquality.di

import com.google.android.gms.location.LocationServices
import lt.kepo.core.model.AirQuality
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.airquality.repository.AirQualityRepositoryImpl
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.airquality.ui.AirQualityActivity
import lt.kepo.airquality.ui.airqualities.AirQualitiesFragment
import lt.kepo.airquality.ui.airqualities.AirQualitiesViewModel
import lt.kepo.airquality.ui.airquality.AirQualityFragment
import lt.kepo.airquality.ui.airquality.AirQualityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val airQualityModule : Module = module {

    scope(named<AirQualityActivity>()) {

        scoped { LocationServices.getFusedLocationProviderClient(androidContext()) }

        factory { UpdateAirQualitiesUseCase(airQualityRepository = get()) }

        factory<AirQualityRepository> { AirQualityRepositoryImpl(
            airQualityDao = get(),
            httpClient = get())
        }
    }

    scope(named<AirQualitiesFragment>()) {
        scoped {
            val activityScope = getKoin().getOrCreateScope(
                named<AirQualityActivity>().toString(),
                named<AirQualityActivity>()
            )

            val vm = AirQualitiesViewModel(
                application = androidApplication(),
                airQualityRepository = activityScope.get(),
                locationClient = activityScope.get(),
                updateAirQualitiesUseCase = activityScope.get()
            )

            activityScope.close()
            vm
        }
    }

    scope(named<AirQualityFragment>()) {
        scoped { (airQuality: AirQuality) -> AirQualityViewModel(
            initAirQuality = airQuality,
            application = androidApplication(),
            airQualityRepository = get(),
            locationClient = get(),
            updateAirQualitiesUseCase = get())
        }
    }
}