package lt.kepo.airq.utility

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import lt.kepo.airq.R

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

inline fun FragmentManager.commitWithAnimations(
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()

    transaction.setCustomAnimations(
        R.anim.fragment_enter,
        R.anim.fragment_exit,
        R.anim.fragment_pop_enter,
        R.anim.fragment_pop_exit
    )
    transaction.body()

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun Fragment.showError(errorMessage: String, container: View) {
    val snack = Snackbar.make(container, errorMessage, Snackbar.LENGTH_LONG)
    val tv = snack.view.findViewById(R.id.snackbar_text) as TextView

    tv.setTextColor(resources.getColor(R.color.colorAccentTint, null))
    snack.view.setBackgroundResource(R.color.colorAccent)
    snack.show()
}