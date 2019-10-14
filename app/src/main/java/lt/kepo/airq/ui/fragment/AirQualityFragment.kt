package lt.kepo.airq.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import lt.kepo.airq.R
import java.text.SimpleDateFormat

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_air_quality, container, false)
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.airQuality.observe(this, airQualityObserver)
        viewModel.errorMessage.observe(this, errorMessageObserver)
        viewModel.isLocationFound.observe(this, isLocationFoundObserver )

        swipeToRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        swipeToRefreshLayout.setOnRefreshListener {
            swipeToRefreshLayout.isRefreshing = true
            viewModel.getRemoteAirQualityHere(requireContext())
        }

        swipeToRefreshLayout.isRefreshing = true
        viewModel.getRemoteAirQualityHere(requireContext())
    }

    override fun onStart() {
        super.onStart()

        airQualityObserver.onChanged(viewModel.airQuality.value)

        activity?.title = viewModel.airQuality.value?.city?.name ?: resources.getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.air_quality_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_stations -> {
                findNavController().navigate(R.id.action_stations)
                true
            }
            R.id.menu_refresh -> {
                swipeToRefreshLayout.isRefreshing = true
                viewModel.getRemoteAirQualityHere(requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val airQualityObserver = Observer<AirQuality> { newAirQuality ->
        activity?.title = newAirQuality?.city?.name

        swipeToRefreshLayout.isRefreshing = false

        textAirQualityIndex.text = newAirQuality?.airQualityIndex
        textCityName.text = newAirQuality?.city?.name
        textDominatingPollutant.text = newAirQuality?.dominatingPollutant

        if (newAirQuality?.time?.localTime != null)
            textTimeRecorded.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newAirQuality.time.localTime)
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        swipeToRefreshLayout.isRefreshing = false

        textError.text = errorMessage
    }

    private val isLocationFoundObserver = Observer<Boolean> { isLocationFound ->
        if (isLocationFound) textLocation.visibility = View.VISIBLE else textLocation.visibility = View.GONE
    }
}