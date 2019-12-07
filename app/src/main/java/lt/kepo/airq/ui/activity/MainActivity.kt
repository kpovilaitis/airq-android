package lt.kepo.airq.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import lt.kepo.airq.R
import lt.kepo.airq.ui.fragment.AirQualitiesFragment
import lt.kepo.airq.utility.COARSE_LOCATION_PERMISSION_CODE


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<FrameLayout>(R.id.main_content).systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_LOCATION_PERMISSION_CODE)
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.main_content, AirQualitiesFragment())
        fragmentTransaction.commit()
    }
}
