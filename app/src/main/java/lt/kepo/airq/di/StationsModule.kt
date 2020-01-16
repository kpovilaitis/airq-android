package lt.kepo.airq.di

import lt.kepo.airq.data.repository.stations.StationsRepository
import lt.kepo.airq.data.repository.stations.StationsRepositoryImpl
import lt.kepo.airq.domain.GetFilteredStationsUseCase
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val stationsModule : Module = module {

    factory<StationsRepository> { StationsRepositoryImpl(httpClient = get()) }

    factory { GetFilteredStationsUseCase(stationsRepository = get()) }

    viewModel { StationsViewModel(
        airQualityRepository = get(),
        getStations = get())
    }
}