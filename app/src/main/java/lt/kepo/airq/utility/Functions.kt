package lt.kepo.airq.utility

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings.Secure
import android.provider.Settings.Secure.LOCATION_MODE_OFF
import android.provider.Settings.Secure.LOCATION_MODE
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun isLocationEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // This is new method provided in API 28
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

        locationManager.isLocationEnabled && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    } else { // This is Deprecated in API 28
        Secure.getInt(context.contentResolver, LOCATION_MODE, LOCATION_MODE_OFF) != LOCATION_MODE_OFF
    }
}