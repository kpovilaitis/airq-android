package lt.kepo.airquality.ui

import android.app.Activity
import androidx.fragment.app.FragmentActivity

interface AppNavigator{
    fun startAirQualityFragment(activity: FragmentActivity)
    fun startStationsActivity(activity: Activity)
}