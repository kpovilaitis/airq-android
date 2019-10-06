package lt.kepo.airq.di

import lt.kepo.airq.repository.stations.StationsRepository
import lt.kepo.airq.repository.stations.StationsRepositoryImpl
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val stationsModule : Module = module {

    factory<StationsRepository> { StationsRepositoryImpl(get(), get()) }

    viewModel { StationsViewModel(get()) }
}