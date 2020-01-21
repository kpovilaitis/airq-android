package lt.kepo.airq

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import lt.kepo.airquality.R
import lt.kepo.airquality.ui.AppNavigator
import lt.kepo.airquality.ui.airqualities.AirQualitiesFragment
import lt.kepo.core.ui.commitWithAnimations
import lt.kepo.stations.ui.StationsActivity

class AirQualitiesNavigatorImpl : AppNavigator {
    override fun startAirQualityFragment(activity: FragmentActivity) {
        activity.supportFragmentManager.commitWithAnimations {
            replace(R.id.main_content, AirQualitiesFragment())
        }
    }

    override fun startStationsActivity(activity: Activity) {
        activity.startActivity(Intent(activity, StationsActivity::class.java))
    }
}