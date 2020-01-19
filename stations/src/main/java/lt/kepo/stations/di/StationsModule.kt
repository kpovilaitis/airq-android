package lt.kepo.stations.di

import lt.kepo.stations.repository.StationsRepository
import lt.kepo.stations.repository.StationsRepositoryImpl
import lt.kepo.stations.ui.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val stationsModule : Module = module {

    factory<StationsRepository> { StationsRepositoryImpl(
        airQualityDao = get(),
        httpClient = get())
    }

    viewModel { StationsViewModel(stationsRepository = get()) }
}