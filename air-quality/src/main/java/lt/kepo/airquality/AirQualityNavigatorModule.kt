package lt.kepo.airquality

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import lt.kepo.core.navigation.AirQualityNavigator

@Module
@InstallIn(ActivityComponent::class)
abstract class AirQualityNavigatorModule {

    @Binds
    abstract fun bindAirQualityNavigator(
        navigatorImpl: AirQualityNavigatorImpl
    ): AirQualityNavigator
}