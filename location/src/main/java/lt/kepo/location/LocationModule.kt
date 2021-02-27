package lt.kepo.location

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context,
    ): LocationClient =
        AndroidLocationClient(
            context = context,
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        )
}
