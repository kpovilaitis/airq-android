package lt.kepo.airquality.airqualities

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val airQualitiesFragmentModule = module {

    scope<AirQualitiesFragment> {

        viewModel {
            AirQualitiesViewModel(
                application = androidApplication(),
                airQualityRepository = get(),
                locationClient = get(),
                updateAirQualitiesUseCase = get()
            )
        }
    }
}
