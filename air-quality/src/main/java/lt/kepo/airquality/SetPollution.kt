package lt.kepo.airquality

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import lt.kepo.core.R

fun View.setPollution(airQualityIndex: String) {
    if (airQualityIndex != "-") {
        val paintDrawable = PaintDrawable()

        paintDrawable.shape = RectShape()
        paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                val indexWorstValue = 300.0f
                var multiplier = airQualityIndex.toFloat() / indexWorstValue

                if (multiplier > 1.0f)
                    multiplier = 1.0f

                return LinearGradient(
                    0f,
                    height.toFloat(),
                    0f,
                    height.toFloat() - ((height.toFloat()) * multiplier),
                    intArrayOf(
                        resources.getColor(R.color.colorPollution, null),
                        resources.getColor(android.R.color.transparent, null)
                    ),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
        }

        background = paintDrawable
    }
}
