package lt.kepo.airqualitydata

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.kepo.airqualitydata.refresh.RefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RefreshAirQualityUseCase
import lt.kepo.airqualitydata.refresh.RemoteRefreshAirQualityHereUseCase
import lt.kepo.airqualitydata.refresh.RemoteRefreshAirQualityUseCase
import lt.kepo.airqualitydata.search.RemoteSearchAirQualitiesUseCase
import lt.kepo.airqualitydata.search.SearchAirQualitiesUseCase
import lt.kepo.airqualitynetwork.AirQualityApi
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Singleton

@Module(
    includes = [
        AirQualityDataModule.Binder::class,
    ]
)
@InstallIn(SingletonComponent::class)
class AirQualityDataModule {

    @Provides
    @Singleton
    internal fun provideAirQualitiesRepository(
        airQualityDao: AirQualityDao,
        refreshAirQuality: RefreshAirQualityUseCase,
        refreshAirQualityHere: RefreshAirQualityHereUseCase,
        airQualityApi: AirQualityApi,
    ): AirQualitiesRepository = DatabaseAirQualitiesRepository(
        airQualityDao = airQualityDao,
        refreshAirQuality = refreshAirQuality,
        refreshAirQualityHere = refreshAirQualityHere,
        airQualityApi = airQualityApi,
        getCurrentTimeMillis = { System.currentTimeMillis() },
    )

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface Binder {

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
}
