package lt.kepo.airq

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import lt.kepo.core.ApplicationScope
import lt.kepo.core.CurrentTime
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AirQualityDataModule {

    @Singleton
    @Provides
    @ApplicationScope
    fun providesCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob())

    @Provides
    @CurrentTime
    fun providesCurrentTime(): () -> Long =
        { System.currentTimeMillis() }
}
