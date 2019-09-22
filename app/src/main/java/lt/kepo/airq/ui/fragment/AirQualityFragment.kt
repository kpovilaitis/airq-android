package lt.kepo.airq.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import lt.kepo.airq.R

class AirQualityFragment : Fragment() {

    private val viewModel: AirQualityViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_air_quality, container, false)
        viewModel.airQuality.observe(this, airQualityObserver)
        viewModel.isLoading.observe(this, isLoadingObserver )
        viewModel.isError.observe(this, isErrorObserver)

//        swipeToRefreshLayout.setOnRefreshListener { viewModel.getRemoteAirQualityHere(requireContext()) }

        viewModel.getLocalAirQualityHere()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.air_quality_fragment_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getRemoteAirQualityHere(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            viewModel.getRemoteAirQualityHere(requireContext())

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private val airQualityObserver = Observer<AirQuality> { newAirQuality ->
        textAirQualityIndex.text = newAirQuality?.airQualityIndex?.toString()
        textCityName.text = newAirQuality?.city?.name
        textDominatingPollutant.text = newAirQuality?.dominatingPollutant
    }

    private val isLoadingObserver = Observer<Boolean> { isLoading ->
        if (isLoading) progress_bar.visibility = View.VISIBLE else progress_bar.visibility = View.GONE
    }

    private val isErrorObserver = Observer<Boolean> { isError ->
        if (isError) textError.visibility = View.VISIBLE else textError.visibility = View.GONE
    }
}