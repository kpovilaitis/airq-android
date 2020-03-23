package lt.kepo.airquality

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.koin.androidx.scope.lifecycleScope

class AirQualityActivity : AppCompatActivity() {
    private val airQualityNavigator: AirQualityNavigator by lifecycleScope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<FrameLayout>(R.id.main_content).systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 104)
        }

        if (savedInstanceState == null) {
            airQualityNavigator.showAirQualities()
        }

        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_window_inverted))
    }
}
