package lt.kepo.stations

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import lt.kepo.core.navigation.StationsNavigator

@Module
@InstallIn(ActivityComponent::class)
abstract class StationsNavigatorModule {

    @Binds
    abstract fun bindStationsNavigator(
        navigatorImpl: StationsNavigatorImpl
    ): StationsNavigator
}