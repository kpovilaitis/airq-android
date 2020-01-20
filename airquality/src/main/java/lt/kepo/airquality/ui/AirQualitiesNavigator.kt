package lt.kepo.airquality.ui

import android.app.Activity

interface AirQualitiesNavigator{
    fun startAirQualityActivity(activity: Activity)
    fun startStationsActivity(activity: Activity)
}