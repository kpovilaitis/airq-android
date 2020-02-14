package lt.kepo.airq

import android.app.Activity
import android.content.Intent
import lt.kepo.airquality.R
import lt.kepo.airquality.ui.AirQualityActivity
import lt.kepo.core.ui.AppNavigator
import lt.kepo.stations.ui.StationsActivity

class AirQualitiesNavigatorImpl : AppNavigator {

    override fun startAirQualityActivity(activity: Activity) {
        activity.apply {
            startActivity(Intent(activity, AirQualityActivity::class.java))
            overridePendingTransition(R.anim.window_enter, R.anim.window_exit)
        }
    }

    override fun startStationsActivity(activity: Activity) {
        activity.apply {
            startActivity(Intent(activity, StationsActivity::class.java))
            overridePendingTransition(R.anim.window_enter, R.anim.window_exit)
        }
    }
}