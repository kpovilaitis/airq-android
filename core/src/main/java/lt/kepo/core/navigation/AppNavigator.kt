package lt.kepo.core.navigation

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class AppNavigator @Inject constructor(
    private val activity: FragmentActivity,
    airQualityNavigator: AirQualityNavigator,
    stationsNavigator: StationsNavigator
) : AirQualityNavigator by airQualityNavigator,
    StationsNavigator by stationsNavigator {

    fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }
}
