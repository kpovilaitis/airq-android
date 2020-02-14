package lt.kepo.stations.di

import lt.kepo.core.database.AppDatabase
import lt.kepo.stations.repository.StationsRepository
import lt.kepo.stations.repository.StationsRepositoryImpl
import lt.kepo.stations.ui.StationsActivity
import lt.kepo.stations.ui.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val stationsModule = module {

    scope(named<StationsActivity>()) {

        scoped { get<AppDatabase>().airQualityDao() }
    }

    factory<StationsRepository> { StationsRepositoryImpl(
        airQualityDao = getScope(StationsActivity.SCOPE_ID).get(),
        httpClient = get())
    }

    viewModel { StationsViewModel(stationsRepository = get()) }
}