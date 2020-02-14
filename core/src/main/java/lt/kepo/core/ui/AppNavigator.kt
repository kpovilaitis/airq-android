package lt.kepo.core.ui

import android.app.Activity

interface AppNavigator{
    fun startAirQualityActivity(activity: Activity)
    fun startStationsActivity(activity: Activity)
}