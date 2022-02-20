package lt.kepo.airqualitydata

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.kepo.airqualitydata.refresh.RefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RefreshAirQualityUseCase
import lt.kepo.airqualitydata.refresh.RemoteRefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RemoteRefreshAirQualityUseCase
import lt.kepo.airqualitydata.search.RemoteSearchAirQualitiesUseCase
import lt.kepo.airqualitydata.search.SearchAirQualitiesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AirQualityDataModule {

    @Binds
    @Singleton
    fun bindAirQualitiesRepository(
        refresh: DatabaseAirQualitiesRepository,
    ): AirQualitiesRepository

    @Binds
    fun bindRefreshAirQualityUseCase(
        refresh: RemoteRefreshAirQualityUseCase,
    ): RefreshAirQualityUseCase

    @Binds
    fun bindRefreshAirQualityHereUseCase(
        refresh: RemoteRefreshAirQualityHereUseCase,
    ): RefreshAirQualityHereUseCase

    @Binds
    fun bindSearchAirQualitiesUseCase(
        getStations: RemoteSearchAirQualitiesUseCase,
    ): SearchAirQualitiesUseCase
}
