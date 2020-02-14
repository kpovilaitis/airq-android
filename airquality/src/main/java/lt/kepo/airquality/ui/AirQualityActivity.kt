package lt.kepo.airquality.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import lt.kepo.airquality.R
import lt.kepo.airquality.ui.airqualities.AirQualitiesFragment
import lt.kepo.core.ui.commitWithAnimations
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.bindScope
import org.koin.core.qualifier.named

class AirQualityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getKoin()
            .createScope(SCOPE_ID, named<AirQualityActivity>())
            .apply { bindScope(this) }

        setContentView(R.layout.activity_main)

        findViewById<FrameLayout>(R.id.main_content).systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 104)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commitWithAnimations {
                replace(R.id.main_content, AirQualitiesFragment())
            }
        }

        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_window_inverted))
    }

    companion object {
        const val SCOPE_ID = "AIR_QUALITY_ACTIVITY_SCOPE_ID"
    }
}
