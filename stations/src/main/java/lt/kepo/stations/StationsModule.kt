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
            addStationUseCase: RemoteSaveStationUseCase
        ): SaveStationUseCase

        @Binds
        fun bindGetStationsUseCase(
            getStations: RemoteSearchStationsUseCase
        ): SearchStationsUseCase
    }
}
