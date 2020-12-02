package lt.kepo.stations

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class StationsModule {

    @Binds
    abstract fun bindAddStationUseCase(
        addStationUseCase: RemoteAddStationUseCase
    ): AddStationUseCase

    @Binds
    abstract fun bindGetStationsUseCase(
        getStations: RemoteGetStationsUseCase
    ): GetStationsUseCase

    @Binds
    abstract fun bindStationsRepository(
        repository: StationsRepositoryImpl
    ): StationsRepository
}
