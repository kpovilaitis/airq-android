package lt.kepo.airq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import lt.kepo.airquality.R
import lt.kepo.core.navigation.AppNavigator
import lt.kepo.core.navigation.NavigationContainer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationContainer {

    @Inject lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 104)
        }

        if (savedInstanceState == null) {
            navigator.showAirQualities()
        }

        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_window_inverted))
    }

    override val id: Int = R.id.main_content
}