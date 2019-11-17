package lt.kepo.airq.ui.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.Fade
import kotlinx.android.synthetic.main.fragment_air_quality.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import lt.kepo.airq.R
import lt.kepo.airq.utility.POSITION_PARCELABLE_KEY
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AirQualityFragment : Fragment() {
    private val viewModel: AirQualityViewModel by inject { parametersOf(arguments?.getParcelable(AirQuality::class.java.simpleName))}

    private lateinit var textCity: AppCompatTextView
    private lateinit var textCountry: AppCompatTextView
    private lateinit var textIndex: AppCompatTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_air_quality, container, false)

        setTransitionNames(fragmentView)
        startNavBarColorAnim()

        return fragmentView
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.airQuality.observe(this, airQualityObserver)
        viewModel.errorMessage.observe(this, errorMessageObserver)

        swipeToRefreshLayout.setOnRefreshListener {
            swipeToRefreshLayout.isRefreshing = true
            viewModel.getRemoteAirQualityHere(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.window?.navigationBarColor = requireContext().getColor(R.color.colorNavigationBar)
    }

    private fun setTransitionNames(fragmentView: View) {
        val elementPosition = arguments?.getInt(POSITION_PARCELABLE_KEY)

        textCity  = fragmentView.findViewById(R.id.textCity)
        textCountry  = fragmentView.findViewById(R.id.textCountry)
        textIndex  = fragmentView.findViewById(R.id.textIndex)

        textCity.transitionName = requireContext().getString(R.string.transition_city_name) + elementPosition
        textCountry.transitionName = requireContext().getString(R.string.transition_country_name) + elementPosition
        textIndex.transitionName = requireContext().getString(R.string.transition_index) + elementPosition
    }

    private fun startNavBarColorAnim() {
        val window = activity?.window
        val from = window?.navigationBarColor
        val to = requireContext().getColor(R.color.colorPollution)
        val navBarColorAnimation = ValueAnimator.ofArgb(from!!, to)

        navBarColorAnimation.addUpdateListener { animator -> window.navigationBarColor = animator.animatedValue as Int }
        navBarColorAnimation.startDelay = 100
        navBarColorAnimation.duration = 400
        navBarColorAnimation.start()
    }

    private val airQualityObserver = Observer<AirQuality> { newAirQuality ->
        swipeToRefreshLayout.isRefreshing = false

        val splitName = newAirQuality?.city?.name?.split(",")?.toMutableList()
        val countryName = splitName?.removeAt(splitName.size - 1)
        val cityName = splitName?.joinToString()?.trimEnd()

        textCity.text = if (cityName?.isEmpty() == true) countryName else cityName
        textCountry.text = countryName?.trimStart()
        textIndex.text = newAirQuality?.airQualityIndex
        textSulfurDioxideValue.text = newAirQuality?.individualIndices?.sulfurOxide?.value.toString()
        textPM25Value.text = newAirQuality?.individualIndices?.particle25?.value.toString()
        textPM10Value.text = newAirQuality?.individualIndices?.particle10?.value.toString()
        textOzoneValue.text = newAirQuality?.individualIndices?.ozone?.value.toString()

        if (newAirQuality?.time?.localTimeRecorded != null)
            textTimeRecordedValue.text = newAirQuality.time.toString()
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        swipeToRefreshLayout.isRefreshing = false
        textError.text = errorMessage
    }
}