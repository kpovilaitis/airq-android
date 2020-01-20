package lt.kepo.airq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lt.kepo.airquality.ui.AirQualityActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, AirQualityActivity::class.java))
    }
}
