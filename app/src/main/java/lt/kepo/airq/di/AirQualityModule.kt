package lt.kepo.airq.di

import lt.kepo.airq.data.repository.airquality.AirQualityRepository
import lt.kepo.airq.data.repository.airquality.AirQualityRepositoryImpl
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val airQualityModule : Module = module {

    factory<AirQualityRepository> { AirQualityRepositoryImpl(get(), get()) }

    viewModel { AirQualityViewModel(get(), get()) }
}