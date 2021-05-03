package lt.kepo.stations

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module(
    includes = [
        StationsModule.ViewModelBinder::class,
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
}
