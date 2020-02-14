package lt.kepo.airquality.di

import com.google.android.gms.location.LocationServices
import lt.kepo.core.model.AirQuality
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.airquality.repository.AirQualityRepositoryImpl
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.airquality.ui.AirQualityActivity
import lt.kepo.airquality.ui.airqualities.AirQualitiesViewModel
import lt.kepo.airquality.ui.airquality.AirQualityViewModel
import lt.kepo.core.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val airQualityModule = module {

    scope(named<AirQualityActivity>()) {

        scoped { get<AppDatabase>().airQualityDao() }

        scoped { LocationServices.getFusedLocationProviderClient(androidContext()) }
    }

    factory { UpdateAirQualitiesUseCase(airQualityRepository = get()) }

    factory<AirQualityRepository> { AirQualityRepositoryImpl(
        airQualityDao = getScope(AirQualityActivity.SCOPE_ID).get(),
        httpClient = get())
    }

    viewModel { AirQualitiesViewModel(
        application = androidApplication(),
        airQualityRepository = get(),
        locationClient = getScope(AirQualityActivity.SCOPE_ID).get(),
        updateAirQualitiesUseCase = get()
    ) }

    viewModel { (airQuality: AirQuality) -> AirQualityViewModel(
        initAirQuality = airQuality,
        application = androidApplication(),
        airQualityRepository = get(),
        locationClient = getScope(AirQualityActivity.SCOPE_ID).get(),
        updateAirQualitiesUseCase = get()
    ) }
}