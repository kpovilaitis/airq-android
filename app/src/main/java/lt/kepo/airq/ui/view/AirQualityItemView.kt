package lt.kepo.airq.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import lt.kepo.airq.R


class AirQualityItemView : ConstraintLayout {
    private lateinit var textCity: AppCompatTextView
    private lateinit var textCountry: AppCompatTextView
    private lateinit var textIndex: AppCompatTextView
    private lateinit var imageBuildings: AppCompatImageView
    private lateinit var imagePollution: View

    constructor(context: Context) : super(context){
        inflate()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        inflate()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate()
    }

    private fun inflate() {
        val view = View.inflate(context, R.layout.view_air_quality_item, this)

        textCity = view.findViewById(R.id.textCity)
        textCountry = view.findViewById(R.id.textCountry)
        textIndex = view.findViewById(R.id.textIndex)
        imageBuildings = view.findViewById(R.id.buildingsImageView)
        imagePollution = view.findViewById(R.id.pollutionView)

//        view.setBackgroundResource(R.drawable.gradient_station_background)
    }
}