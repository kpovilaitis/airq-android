package lt.kepo.airquality.ui.airquality

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airquality.R
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID
import lt.kepo.core.model.AirQuality
import lt.kepo.core.ui.setFullName
import lt.kepo.core.ui.setPollution
import lt.kepo.core.ui.showError
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by inject {
        parametersOf(arguments?.getParcelable(AirQuality::class.java.simpleName))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_air_quality, container, false)

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

            swipeToRefreshLayout.setProgressViewOffset(true, statusBarHeight, statusBarHeight * 2)

            container.setPadding(insets.systemWindowInsetLeft, 0, insets.systemWindowInsetRight, 0)
            textTimeRecordedLabel.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            textTimeRecordedValue.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
            v.setPadding(0, statusBarHeight, 0 , 0)
            insets
        }

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        textCity.setOnClickListener { showLocationNameDialog() }
        textCountry.setOnClickListener { showLocationNameDialog() }

        swipeToRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeToRefreshLayout.setColorSchemeResources(R.color.colorAccentTint)
        swipeToRefreshLayout.setOnRefreshListener { viewModel.updateCachedAirQuality(true) }

        viewModel.airQuality.observe(viewLifecycleOwner, airQualityObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, progressObserver)
    }

    override fun onStart() {
        super.onStart()

        viewModel.updateCachedAirQuality()
    }

    private fun formatText(templateResId: Int, value: Any?) =
        resources.getString(templateResId, value?.toString() ?: "-")

    private fun showLocationNameDialog() {
        AlertDialog.Builder(requireActivity(), R.style.Dialog).apply {
            setTitle(R.string.dialog_title_location_name)
            setMessage(viewModel.airQuality.value?.city?.name)
            setPositiveButton(R.string.close) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    private val airQualityObserver = Observer<AirQuality> { it?.let { newAirQuality ->
            if (newAirQuality.stationId == AIR_QUALITY_HERE_STATION_ID) {
                btnRemoveAirQuality.visibility = View.GONE
            } else {
                btnRemoveAirQuality.setOnClickListener {
                    viewModel.removeAirQuality()
                    parentFragmentManager.popBackStack()
                }
            }

            setFullName(newAirQuality.city.name, textCity, textCountry)
            textIndex.text = newAirQuality.airQualityIndex

            textSulfurDioxideValue.text = formatText(R.string.template_ppb, newAirQuality.individualIndices.sulfurOxide?.value)
            textPM25Value.text = formatText(R.string.template_μgm, newAirQuality.individualIndices.particle25?.value)
            textPM10Value.text = formatText(R.string.template_μgm, newAirQuality.individualIndices.particle10?.value)
            textOzoneValue.text = formatText(R.string.template_ppb, newAirQuality.individualIndices.ozone?.value)
            textTimeRecordedValue.text = newAirQuality.time.toString()
            pollutionView.setPollution(newAirQuality.airQualityIndex)
        }
    }

    private val errorMessageObserver = Observer<String> { it?.let { errorMessage -> container.showError(errorMessage) } }

    private val progressObserver = Observer<Boolean> { swipeToRefreshLayout.isRefreshing = it }
}