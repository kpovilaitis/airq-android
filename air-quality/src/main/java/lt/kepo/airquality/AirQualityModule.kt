package lt.kepo.airquality

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        AirQualityModule.ViewModelBinder::class,
    ]
)
@InstallIn(SingletonComponent::class)
class AirQualityModule {

    @Provides
    @Singleton
    internal fun provideExpiringAirQualitiesRepository(
        repository: DatabaseAirQualitiesRepository
    ): ExpiringAirQualitiesRepository = ExpiringAirQualitiesRepository(
        originalRepository = repository,
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
}
