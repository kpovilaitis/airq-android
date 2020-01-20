package lt.kepo.airq

import android.app.Activity
import android.content.Intent
import lt.kepo.airquality.ui.AirQualitiesNavigator
import lt.kepo.airquality.ui.AirQualityActivity
import lt.kepo.stations.ui.StationsActivity

class AirQualitiesNavigatorImpl : AirQualitiesNavigator {
    override fun startAirQualityActivity(activity: Activity) {
        activity.startActivity(Intent(activity, AirQualityActivity::class.java))
    }

    override fun startStationsActivity(activity: Activity) {
        activity.startActivity(Intent(activity, StationsActivity::class.java))
    }
}