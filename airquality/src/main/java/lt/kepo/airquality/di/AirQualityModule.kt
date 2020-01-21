package lt.kepo.airquality.di

import com.google.android.gms.location.LocationServices
import lt.kepo.core.model.AirQuality
import lt.kepo.airquality.repository.AirQualityRepository
import lt.kepo.airquality.repository.AirQualityRepositoryImpl
import lt.kepo.airquality.domain.UpdateAirQualitiesUseCase
import lt.kepo.airquality.ui.airqualities.AirQualitiesViewModel
import lt.kepo.airquality.ui.airquality.AirQualityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val airQualityModule : Module = module {

    single { LocationServices.getFusedLocationProviderClient(androidContext()) }

    factory { UpdateAirQualitiesUseCase(airQualityRepository = get()) }

    factory<AirQualityRepository> { AirQualityRepositoryImpl(
        airQualityDao = get(),
        httpClient = get())
    }

    viewModel { AirQualitiesViewModel(
        application = androidApplication(),
        airQualityRepository = get(),
        locationClient = get(),
        updateAirQualitiesUseCase = get()
    ) }

    viewModel { (airQuality: AirQuality) -> AirQualityViewModel(
        initAirQuality = airQuality,
        application = androidApplication(),
        airQualityRepository = get(),
        locationClient = get(),
        updateAirQualitiesUseCase = get()
    ) }
}