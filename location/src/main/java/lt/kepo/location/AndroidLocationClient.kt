package lt.kepo.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidLocationClient(
    private val context: Context,
    private val fusedLocationClient : FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun getLastLocation(): Location? = withContext(Dispatchers.Default) {
        suspendCoroutine { continuation ->
            if (isLocationEnabled()) {
                fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                    if (locationAvailability.isLocationAvailable) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            continuation.resume(location?.toDomainLocation())
                        }
                    } else {
                        continuation.resume(null)
                    }
                }
            } else {
                continuation.resume(null)
            }
        }
    }

    private fun isLocationEnabled(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // This is new method provided in API 28
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                .isLocationEnabled
        } else { // This is Deprecated in API 28
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            ) != Settings.Secure.LOCATION_MODE_OFF
        }

    private fun android.location.Location.toDomainLocation(): Location =
        Location(
            latitude = latitude,
            longitude = longitude
        )
}