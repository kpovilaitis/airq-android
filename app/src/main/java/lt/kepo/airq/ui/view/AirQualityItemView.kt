package lt.kepo.airq.ui.view

import android.content.Context
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import lt.kepo.airq.R
import android.graphics.Shader.TileMode
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.PaintDrawable


class AirQualityItemView : ConstraintLayout {
    private lateinit var textCity: AppCompatTextView
    private lateinit var textCountry: AppCompatTextView
    private lateinit var textIndex: AppCompatTextView
    private lateinit var imagePollution: View

    constructor(context: Context) : super(context){
        inflate()
        setPollution(0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        inflate()
        setPollution(0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate()
        setPollution(0)
    }

    fun setPollution(index: Int) {
        val paintDrawable = PaintDrawable()

        paintDrawable.shape = RectShape()
        paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
            override fun resize(width: Int, height: Int): Shader {
                return LinearGradient(
                    0f,
                    height.toFloat(),
                    0f,
                    height.toFloat() - ((height.toFloat()) * index.toFloat() / resources.getInteger(R.integer.pollution_max_value).toFloat()),
                    intArrayOf(resources.getColor(R.color.colorPollution, null), resources.getColor(android.R.color.transparent, null)),
                    null,
                    TileMode.CLAMP
                )
            }
        }

        background = paintDrawable
    }

    private fun inflate() {
        val view = View.inflate(context, R.layout.view_air_quality_item, this)

        textCity = view.findViewById(R.id.textCity)
        textCountry = view.findViewById(R.id.textCountry)
        textIndex = view.findViewById(R.id.textIndex)
        imagePollution = view.findViewById(R.id.pollutionView)
    }
}