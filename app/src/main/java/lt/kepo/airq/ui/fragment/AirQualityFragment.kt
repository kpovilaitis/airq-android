package lt.kepo.airq.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.R
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.airQuality.observe(this, airQualityObserver)
        viewModel.isLoading.observe(this, isLoadingObserver )

        return inflater.inflate(R.layout.fragment_air_quality, container, false)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getRemoteAirQualityHere()
    }

    private val airQualityObserver = Observer<AirQuality> { newAirQuality ->
        textAirQualityIndex.text = newAirQuality.airQualityIndex.toString()
        textCityName.text = newAirQuality.city.name
        textDominatingPollutant.text = newAirQuality.dominatingPollutant
    }

    private val isLoadingObserver = Observer<Boolean> { isLoading ->
        if (isLoading) progress_bar.visibility = View.VISIBLE else progress_bar.visibility = View.GONE
    }
}