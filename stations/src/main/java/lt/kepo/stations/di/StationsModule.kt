package lt.kepo.stations.di

import lt.kepo.core.database.AppDatabase
import lt.kepo.core.ui.AppNavigator
import lt.kepo.stations.repository.StationsRepository
import lt.kepo.stations.repository.StationsRepositoryImpl
import lt.kepo.stations.ui.StationsActivity
import lt.kepo.stations.ui.StationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val stationsModule = module {

    scope(named<StationsActivity>()) {

        scoped { get<AppDatabase>().airQualityDao() }

        scoped { getKoin()._scopeRegistry.rootScope.get<AppNavigator> { parametersOf(get<StationsActivity>()) } }

        factory<StationsRepository> { StationsRepositoryImpl(
            airQualityDao = get(),
            httpClient = get())
        }

        viewModel { StationsViewModel(stationsRepository = get()) }
    }
}
