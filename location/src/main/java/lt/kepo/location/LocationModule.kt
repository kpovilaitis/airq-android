package lt.kepo.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
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
