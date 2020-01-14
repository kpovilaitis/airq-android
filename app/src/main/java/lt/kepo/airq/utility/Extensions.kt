package lt.kepo.airq.utility

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import java.util.*

fun View.setPollution(airQualityIndex: String?) {
    if (airQualityIndex != null && airQualityIndex != "-") {
        val paintDrawable = PaintDrawable()

        paintDrawable.shape = RectShape()
        paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                val indexWorstValue = resources.getInteger(R.integer.index_worst_value).toFloat()
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