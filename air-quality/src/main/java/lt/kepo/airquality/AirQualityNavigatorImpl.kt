package lt.kepo.airquality

import android.os.Bundle
import androidx.fragment.app.*
import lt.kepo.airquality.airqualitydetails.AirQualityDetailsFragment
import lt.kepo.airquality.airqualities.AirQualitiesFragment
import lt.kepo.core.navigation.AirQualityNavigator
import lt.kepo.core.navigation.NavigationContainer
import javax.inject.Inject

class AirQualityNavigatorImpl @Inject constructor(
    private val fragmentManager: FragmentManager,
    private val container: NavigationContainer
) : AirQualityNavigator {

    override fun showAirQualities() =
        fragmentManager.commit {
            replace(container.id, AirQualitiesFragment())
        }

    override fun showAirQualityDetails(stationId: Int) {
        val bundle = Bundle().apply {
            putInt("station_id", stationId)
        }
        val nextFragment = AirQualityDetailsFragment().apply {
            arguments = bundle
        }

        fragmentManager.commit{
            replace(container.id, nextFragment)
            addToBackStack(null)
        }
    }

    private inline fun FragmentManager.commitWithAnimations(
        body: FragmentTransaction.() -> Unit
    ) {
        val transaction = beginTransaction()

        transaction.setCustomAnimations(
            lt.kepo.core.R.anim.window_enter,
            lt.kepo.core.R.anim.window_exit,
            lt.kepo.core.R.anim.window_pop_enter,
            lt.kepo.core.R.anim.window_pop_exit
        )
        transaction.body()
        transaction.commit()
    }
}
