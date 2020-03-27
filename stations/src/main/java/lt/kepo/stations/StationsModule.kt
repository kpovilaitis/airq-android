package lt.kepo.stations

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import lt.kepo.core.navigation.StationsNavigator

@Module
@InstallIn(ActivityComponent::class)
abstract class StationsModule {

    @Binds
    abstract fun bindStationsNavigator(
        navigatorImpl: StationsNavigatorImpl
    ): StationsNavigator
}

@Module
@InstallIn(ActivityComponent::class)
abstract class StationsInternalModule {

    @Binds
    abstract fun bindAddStationUseCase(
        addStationUseCase: RemoteAddStationUseCase
    ): AddStationUseCase

    @Binds
    abstract fun bindGetStationsUseCase(
        getStations: RemoteGetStationsUseCase
    ): GetStationsUseCase
}