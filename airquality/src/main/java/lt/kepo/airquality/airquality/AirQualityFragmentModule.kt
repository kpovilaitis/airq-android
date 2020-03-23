package lt.kepo.airquality.airquality

import lt.kepo.core.model.AirQuality
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val airQualityFragmentModule = module {

    scope(named<AirQualityFragment>()) {

        viewModel { (airQuality: AirQuality) ->
            AirQualityViewModel(
                initAirQuality = airQuality,
                application = androidApplication(),
                airQualityRepository = get(),
                locationClient = get(),
                updateAirQualitiesUseCase = get()
            )
        }
    }
}
