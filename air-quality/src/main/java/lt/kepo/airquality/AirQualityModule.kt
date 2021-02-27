package lt.kepo.airquality

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.core.navigation.AirQualityNavigator
import javax.inject.Singleton

@Module(
    includes = [
        AirQualityModule.ViewModelBinder::class,
        AirQualityModule.ActivityBinder::class
    ]
)
@InstallIn(SingletonComponent::class)
class AirQualityModule {

    @Provides
    @Singleton
    internal fun provideExpiringAirQualitiesRepository(
        airQualityDao: AirQualityDao,
        refreshAirQuality: RefreshAirQualityUseCase,
        refreshAirQualityHere: RefreshAirQualityHereUseCase
    ): ExpiringAirQualitiesRepository = DatabaseAirQualitiesRepository(
        airQualityDao = airQualityDao,
        refreshAirQuality = refreshAirQuality,
        refreshAirQualityHere = refreshAirQualityHere
    ).withExpiration(
        expiresAfterMillis = 30 * 60 * 1000,
        getCurrentTimeMillis = { System.currentTimeMillis() }
    )

    @Module
    @InstallIn(ViewModelComponent::class)
    internal interface ViewModelBinder {

        @Binds
        fun bindRefreshAirQualityUseCase(
            refresh: RemoteRefreshAirQualityUseCase
        ): RefreshAirQualityUseCase

        @Binds
        fun bindRefreshAirQualityHereUseCase(
            refresh: RemoteRefreshAirQualityHereUseCase
        ): RefreshAirQualityHereUseCase

        @Binds
        fun bindIsAirQualitiesExpired(
            repository: ExpiringAirQualitiesRepository
        ): IsAirQualitiesExpired

        @Binds
        fun bindAirQualitiesRepository(
            repository: ExpiringAirQualitiesRepository
        ): AirQualitiesRepository
    }

    @Module
    @InstallIn(ActivityComponent::class)
    abstract class ActivityBinder {

        @Binds
        abstract fun bindAirQualityNavigator(
            navigatorImpl: AirQualityNavigatorImpl
        ): AirQualityNavigator
    }
}
