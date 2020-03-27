package lt.kepo.airquality

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import lt.kepo.airquality.airqualities.AirQualitiesRepository
import lt.kepo.airquality.airqualities.AirQualitiesRepositoryImpl
import lt.kepo.airquality.airqualitydetails.AirQualityRepository
import lt.kepo.airquality.airqualitydetails.AirQualityRepositoryImpl
import lt.kepo.core.navigation.AirQualityNavigator

@Module
@InstallIn(ActivityComponent::class)
abstract class AirQualityModule {

    @Binds
    abstract fun bindAirQualityNavigator(
        navigatorImpl: AirQualityNavigatorImpl
    ): AirQualityNavigator
}

@Module
@InstallIn(ActivityComponent::class)
abstract class AirQualityInternalModule {

    @Binds
    abstract fun bindRefreshAirQualityUseCase(
        refresh: RemoteRefreshAirQualityUseCase
    ): RefreshAirQualityUseCase

    @Binds
    abstract fun bindRefreshAirQualityHereUseCase(
        refresh: RemoteRefreshAirQualityHereUseCase
    ): RefreshAirQualityHereUseCase

    @Binds
    abstract fun bindAirQualitiesRepository(
        repository: AirQualitiesRepositoryImpl
    ): AirQualitiesRepository

    @Binds
    abstract fun bindAirQualityRepository(
        repository: AirQualityRepositoryImpl
    ): AirQualityRepository
}