package lt.kepo.airquality

import com.google.android.gms.location.LocationServices
import lt.kepo.airquality.domain.AirQualityRepository
import lt.kepo.airquality.domain.AirQualityRepositoryImpl
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.core.database.AppDatabase
import lt.kepo.core.ui.AppNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val airQualityActivityModule = module {

    scope(named<AirQualityActivity>()) {

        scoped { get<AppDatabase>().airQualityDao() }

        scoped { LocationServices.getFusedLocationProviderClient(androidContext()) }

        scoped { AirQualityNavigator(get<AirQualityActivity>().supportFragmentManager) }

        scoped { getKoin()._scopeRegistry.rootScope.get<AppNavigator> { parametersOf(get<AirQualityActivity>()) } }

        factory { UpdateAirQualitiesUseCase(airQualityRepository = get()) }

        factory<AirQualityRepository> {
            AirQualityRepositoryImpl(
                airQualityDao = get(),
                httpClient = get()
            )
        }
    }
}
