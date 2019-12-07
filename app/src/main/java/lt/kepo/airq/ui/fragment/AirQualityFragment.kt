package lt.kepo.airq.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import lt.kepo.airq.R
import lt.kepo.airq.utility.POSITION_PARCELABLE_KEY
import lt.kepo.airq.utility.setFullName
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by inject { parametersOf(arguments?.getParcelable(AirQuality::class.java.simpleName)) }

    private lateinit var swipeToRefreshLayout: SwipeRefreshLayout
    private lateinit var textCity: AppCompatTextView
    private lateinit var textCountry: AppCompatTextView
    private lateinit var textIndex: AppCompatTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_air_quality, container, false)

        swipeToRefreshLayout = fragmentView.findViewById(R.id.swipeToRefreshLayout)
        textCity  = fragmentView.findViewById(R.id.textCity)
        textCountry  = fragmentView.findViewById(R.id.textCountry)
        textIndex  = fragmentView.findViewById(R.id.textIndex)

        fragmentView.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

            swipeToRefreshLayout.setProgressViewOffset(false, 0, statusBarHeight * 2)

            v.setPadding(0, statusBarHeight, 0 ,0)
            insets
        }

        setTransitionNames()

        return fragmentView
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.airQuality.value?.isCurrentLocationQuality == true) {
            btnRemoveAirQuality.visibility = View.GONE
        } else {
            btnRemoveAirQuality.setOnClickListener {
                viewModel.removeAirQuality()
                parentFragmentManager.popBackStack()
            }
        }

        viewModel.airQuality.observe(this, airQualityObserver)
        viewModel.errorMessage.observe(this, errorMessageObserver)

        swipeToRefreshLayout.setOnRefreshListener {
            swipeToRefreshLayout.isRefreshing = true
            viewModel.updateAirQuality()
        }
    }

    override fun onStop() {
        super.onStop()

        textCity.visibility = View.GONE
        textCountry.visibility = View.GONE
        textIndex.visibility = View.GONE
    }

    private fun setTransitionNames() {
        val elementPosition = arguments?.getInt(POSITION_PARCELABLE_KEY)

        textCity.transitionName = requireContext().getString(R.string.transition_city_name) + elementPosition
        textCountry.transitionName = requireContext().getString(R.string.transition_country_name) + elementPosition
        textIndex.transitionName = requireContext().getString(R.string.transition_index) + elementPosition
    }

    private fun setAirQualityText(newAirQuality: AirQuality?) {
        setFullName(newAirQuality?.city?.name, textCity, textCountry)
        textIndex.text = newAirQuality?.airQualityIndex

        textSulfurDioxideValue.text = formatText(R.string.template_ppb, newAirQuality?.individualIndices?.sulfurOxide?.value)
        textPM25Value.text = formatText(R.string.template_μgm, newAirQuality?.individualIndices?.particle25?.value)
        textPM10Value.text = formatText(R.string.template_μgm, newAirQuality?.individualIndices?.particle10?.value)
        textOzoneValue.text = formatText(R.string.template_ppb, newAirQuality?.individualIndices?.ozone?.value)
        textTimeRecordedValue.text = formatText(R.string.template_hours, newAirQuality?.time)
    }

    private fun formatText(templateResId: Int, value: Any?): String {
        return resources.getString(templateResId, value?.toString() ?: "-")
    }

    private val airQualityObserver = Observer<AirQuality> { newAirQuality ->
        swipeToRefreshLayout.isRefreshing = false
        setAirQualityText(newAirQuality)
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        swipeToRefreshLayout.isRefreshing = false
        textError.text = errorMessage
    }
}