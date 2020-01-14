package lt.kepo.airq.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_air_quality.*
import kotlinx.android.synthetic.main.fragment_air_quality.swipeToRefreshLayout
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import lt.kepo.airq.R
import lt.kepo.airq.utility.setFullName
import lt.kepo.airq.utility.setPollution
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by inject { parametersOf(arguments?.getParcelable(AirQuality::class.java.simpleName)) }

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

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        swipeToRefreshLayout.setOnRefreshListener {
            swipeToRefreshLayout.isRefreshing = true

            viewModel.updateAirQuality()
        }

        viewModel.airQuality.observe(viewLifecycleOwner, airQualityObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private fun formatText(templateResId: Int, value: Any?): String {
        return resources.getString(templateResId, value?.toString() ?: "-")
    }

    private val airQualityObserver = Observer<AirQuality> { it?.let { newAirQuality ->
            if (newAirQuality.isCurrentLocationQuality) {
                btnRemoveAirQuality.visibility = View.GONE
            } else {
                btnRemoveAirQuality.setOnClickListener {
                    viewModel.removeAirQuality()
                    parentFragmentManager.popBackStack()
                }
            }

            swipeToRefreshLayout.isRefreshing = false

            setFullName(newAirQuality.city.name, textCity, textCountry)
            textIndex.text = newAirQuality.airQualityIndex

            textSulfurDioxideValue.text = formatText(R.string.template_ppb, newAirQuality.individualIndices.sulfurOxide?.value)
            textPM25Value.text = formatText(R.string.template_μgm, newAirQuality.individualIndices.particle25?.value)
            textPM10Value.text = formatText(R.string.template_μgm, newAirQuality.individualIndices.particle10?.value)
            textOzoneValue.text = formatText(R.string.template_ppb, newAirQuality.individualIndices.ozone?.value)
            textTimeRecordedValue.text = newAirQuality.time.toString()
            pollutionView.setPollution(newAirQuality.airQualityIndex)

            if (newAirQuality.shouldUpdate()) {
                viewModel.updateAirQuality()
            }
        }
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        swipeToRefreshLayout.isRefreshing = false
        textError.text = errorMessage
    }
}