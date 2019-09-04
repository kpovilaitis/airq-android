package lt.kepo.airq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.R
import lt.kepo.airq.viewmodels.AirQualityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirQualityFragment : Fragment() {

    private val viewModel: AirQualityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_air_quality, container, false)

        viewModel.airQuality.observe(this, Observer {newAirQuality ->
            textAirQualityIndex.text = newAirQuality.airQualityIndex.toString()
            textCityName.text = newAirQuality.city.name
            textDominatingPollutant.text = newAirQuality.dominatingPollutant
        })

        viewModel.getAirQuality()

        return view
    }
}