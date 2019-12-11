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
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import lt.kepo.airq.R

fun isLocationEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // This is new method provided in API 28
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

        locationManager.isLocationEnabled && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    } else { // This is Deprecated in API 28
        Secure.getInt(context.contentResolver, LOCATION_MODE, LOCATION_MODE_OFF) != LOCATION_MODE_OFF
    }
}

fun getListDivider(context: Context, drawableId : Int): DividerItemDecoration {
    val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

    divider.setDrawable(context.getDrawable(drawableId)!!)

    return divider
}

fun setFullName(locationName: String?, textCity: AppCompatTextView, textCountry: AppCompatTextView) {
    var bracelessLocationName = locationName?.replace("(", "")
    bracelessLocationName = bracelessLocationName?.replace(")", "")
    val splitName = bracelessLocationName?.split(",")?.toMutableList()
    val countryName = splitName?.removeAt(splitName.size - 1)
    val cityName = splitName?.joinToString()?.trimEnd()

    textCity.text = if (cityName?.isEmpty() == true) countryName else cityName
    textCountry.text = countryName?.trimStart()
}