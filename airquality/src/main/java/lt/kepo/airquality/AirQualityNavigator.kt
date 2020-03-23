package lt.kepo.airquality

import android.os.Bundle
import androidx.fragment.app.*
import lt.kepo.airquality.airquality.AirQualityFragment
import lt.kepo.airquality.airqualities.AirQualitiesFragment
import lt.kepo.core.model.AirQuality

class AirQualityNavigator (
    private val fragmentManager: FragmentManager
) {
    fun showAirQualities() =
        fragmentManager.commitWithAnimations {
            replace(R.id.main_content,
                AirQualitiesFragment()
            )
        }

    fun showAirQuality(airQuality: AirQuality) {
        val bundle = Bundle().apply { putParcelable(AirQuality::class.java.simpleName, airQuality) }
        val nextFragment = AirQualityFragment().apply { arguments = bundle }

        fragmentManager.commitWithAnimations{
            replace(R.id.main_content, nextFragment)
            addToBackStack(null)
        }
    }

    fun goBack() = fragmentManager.popBackStack()

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
