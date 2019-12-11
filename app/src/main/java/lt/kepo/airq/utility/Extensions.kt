package lt.kepo.airq.utility

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import lt.kepo.airq.R

fun View.setPollution(index: Int) {
    val paintDrawable = PaintDrawable()

    paintDrawable.shape = RectShape()
    paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
        override fun resize(width: Int, height: Int): Shader {
            return LinearGradient(
                0f,
                height.toFloat(),
                0f,
                height.toFloat() - ((height.toFloat()) * index.toFloat() / resources.getInteger(
                    R.integer.pollution_max_value).toFloat()),
                intArrayOf(resources.getColor(R.color.colorPollution, null), resources.getColor(android.R.color.transparent, null)),
                null,
                Shader.TileMode.CLAMP
            )
        }
    }

    background = paintDrawable
}