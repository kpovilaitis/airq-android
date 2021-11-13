package lt.kepo.airq

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AirQualityDataModule {

    @Named("application")
    @Provides
    @Singleton
    internal fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.Default
        )
}
