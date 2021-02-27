package lt.kepo.stations

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import lt.kepo.core.navigation.StationsNavigator

@Module(
    includes = [
        StationsModule.ViewModelBinder::class,
        StationsModule.ActivityBinder::class
    ]
)
@InstallIn(ViewModelComponent::class)
class StationsModule {

    @Module
    @InstallIn(ViewModelComponent::class)
    interface ViewModelBinder {

        @Binds
        fun bindAddStationUseCase(
            addStationUseCase: RemoteAddStationUseCase
        ): AddStationUseCase

        @Binds
        fun bindGetStationsUseCase(
            getStations: RemoteGetStationsUseCase
        ): GetStationsUseCase

        @Binds
        fun bindStationsRepository(
            repository: RemoteStationsRepository
        ): StationsRepository
    }

    @Module
    @InstallIn(ActivityComponent::class)
    interface ActivityBinder {

        @Binds
        fun bindStationsNavigator(
            navigatorImpl: StationsNavigatorImpl
        ): StationsNavigator
    }
}
