package lt.kepo.stations

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import dagger.hilt.android.qualifiers.ActivityContext
import lt.kepo.core.navigation.NavigationContainer
import lt.kepo.core.navigation.StationsNavigator
import javax.inject.Inject

class StationsNavigatorImpl @Inject constructor(
    private val fragmentManager: FragmentManager,
    private val container: NavigationContainer
) : StationsNavigator {

    override fun showStations() {
        fragmentManager.commit {
            replace(container.id, StationsFragment())
        }
    }
}
