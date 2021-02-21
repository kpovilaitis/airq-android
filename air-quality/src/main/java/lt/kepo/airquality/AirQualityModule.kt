package lt.kepo.airquality

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import lt.kepo.airqualitydatabase.AirQualityDao
import javax.inject.Singleton

@Module(
    includes = [
        AirQualityModule.Binder::class
    ]
)
@InstallIn(ActivityRetainedComponent::class)
class AirQualityModule {

    @Provides
    @ActivityRetainedScoped
    internal fun provideCachedAirQualitiesRepository(
        airQualityDao: AirQualityDao,
        refreshAirQuality: RefreshAirQualityUseCase,
        refreshAirQualityHere: RefreshAirQualityHereUseCase
    ): CachedAirQualitiesRepository = DatabaseAirQualitiesRepository(
        airQualityDao = airQualityDao,
        refreshAirQuality = refreshAirQuality,
        refreshAirQualityHere = refreshAirQualityHere
    ).withCache(
        cacheExpiresAfterMillis = 30 * 60 * 1000,
        getCurrentTimeMillis = { System.currentTimeMillis() }
    )

    @Module
    @InstallIn(ActivityRetainedComponent::class)
    internal interface Binder {

        @Binds
        fun bindRefreshAirQualityUseCase(
            refresh: RemoteRefreshAirQualityUseCase
        ): RefreshAirQualityUseCase

        @Binds
        fun bindRefreshAirQualityHereUseCase(
            refresh: RemoteRefreshAirQualityHereUseCase
        ): RefreshAirQualityHereUseCase

        @Binds
        fun bindRefreshAirQualitiesCacheUseCase(
            repository: CachedAirQualitiesRepository
        ): RefreshAirQualitiesCacheUseCase

        @Binds
        fun bindAirQualitiesRepository(
            repository: CachedAirQualitiesRepository
        ): AirQualitiesRepository
    }
}
