package lt.kepo.airq

import android.app.Activity
import android.content.Intent
import android.os.Build
import lt.kepo.airquality.R
import lt.kepo.airquality.AirQualityActivity
import lt.kepo.core.ui.AppNavigator
import lt.kepo.stations.ui.StationsActivity

class AppNavigatorImpl(
    private val activity: Activity
) : AppNavigator {

    override fun startAirQuality() =
        activity.run {
            startActivity(Intent(activity, AirQualityActivity::class.java))
            overrideOldSdkTransition()
        }

    override fun startStations() =
        activity.run {
            startActivity(Intent(activity, StationsActivity::class.java))
            overrideOldSdkTransition()
        }

    override fun finishActivity(resultCode: Int?)  =
        activity.run {
            resultCode?.run { setResult(this) }
            finish()
            overridePendingTransition(R.anim.window_pop_enter, lt.kepo.stations.R.anim.window_pop_exit)
        }

    private fun Activity.overrideOldSdkTransition() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) { // overriding default anim in new android looks bad
            overridePendingTransition(R.anim.window_enter, R.anim.window_exit)
        }
    }
}