package lt.kepo.airquality.airqualitydetails

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_air_quality.*
import kotlinx.android.synthetic.main.fragment_air_quality.swipeToRefreshLayout
import kotlinx.android.synthetic.main.fragment_air_quality.textCity
import kotlinx.android.synthetic.main.fragment_air_quality.textCountry
import lt.kepo.airquality.R
import lt.kepo.airquality.setPollution
import lt.kepo.airquality.airqualitydetails.AirQualityDetailsViewModel.Error
import lt.kepo.core.navigation.AppNavigator
import lt.kepo.core.ui.showError
import javax.inject.Inject

@AndroidEntryPoint
class AirQualityDetailsFragment : Fragment(R.layout.fragment_air_quality) {

    @Inject lateinit var navigator: AppNavigator
    private val viewModel: AirQualityDetailsViewModel by viewModels()

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnApplyWindowInsetsListener { v, insets ->
            val statusBarHeight = insets.systemWindowInsetTop

            swipeToRefreshLayout.setProgressViewOffset(true, statusBarHeight, statusBarHeight * 2)

            air_quality_container.setPadding(insets.systemWindowInsetLeft, 0, insets.systemWindowInsetRight, 0)
            textTimeRecordedLabel.setPadding(textTimeRecordedLabel.paddingLeft, textTimeRecordedLabel.paddingTop, textTimeRecordedLabel.paddingRight, insets.systemWindowInsetBottom)
            textTimeRecordedValue.setPadding(textTimeRecordedLabel.paddingLeft, textTimeRecordedLabel.paddingTop, textTimeRecordedLabel.paddingRight, insets.systemWindowInsetBottom)
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textOzoneLabel.setPadding(textTimeRecordedLabel.paddingLeft, textTimeRecordedLabel.paddingTop, textTimeRecordedLabel.paddingRight, insets.systemWindowInsetBottom)
                textOzoneValue.setPadding(textTimeRecordedLabel.paddingLeft, textTimeRecordedLabel.paddingTop, textTimeRecordedLabel.paddingRight, insets.systemWindowInsetBottom)
            }
            v.setPadding(0, statusBarHeight, 0 , 0)
            insets
        }

        btnBack.setOnClickListener { navigator.goBack() }
        btnRemoveAirQuality.setOnClickListener {
            viewModel.removeAirQuality()
            parentFragmentManager.popBackStack()
        }
        textCity.setOnClickListener { showLocationNameDialog() }
        textCountry.setOnClickListener { showLocationNameDialog() }

        textPM25Label.setOnClickListener { showExplanationDialog(R.string.dialog_title_pm25, R.string.dialog_message_pm25) }
        textPM25Value.setOnClickListener { showExplanationDialog(R.string.dialog_title_pm25_levels, R.string.dialog_message_pm25_levels) }
        textPM10Label.setOnClickListener { showExplanationDialog(R.string.dialog_title_pm10, R.string.dialog_message_pm10) }
        textPM10Value.setOnClickListener { showExplanationDialog(R.string.dialog_title_pm10_levels, R.string.dialog_message_pm10_levels) }
        textSulfurDioxideLabel.setOnClickListener { showExplanationDialog(R.string.dialog_title_sulfur_dioxide, R.string.dialog_message_sulfur_dioxide) }
        textSulfurDioxideValue.setOnClickListener { showExplanationDialog(R.string.dialog_title_sulfur_dioxide_levels, R.string.dialog_message_sulfur_dioxide_levels) }
        textOzoneLabel.setOnClickListener { showExplanationDialog(R.string.dialog_title_ozone, R.string.dialog_message_ozone) }
        textOzoneValue.setOnClickListener { showExplanationDialog(R.string.dialog_title_ozone_levels, R.string.dialog_message_ozone_levels) }
        textTimeRecordedLabel.setOnClickListener { showExplanationDialog(R.string.dialog_title_recorded_at, R.string.dialog_message_recorded_at) }

        swipeToRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeToRefreshLayout.setColorSchemeResources(R.color.colorAccentTint)
        swipeToRefreshLayout.setOnRefreshListener { viewModel.refreshAirQuality() }

        viewModel.isRemoveAirQualityVisible.observe(viewLifecycleOwner) { isRemoveAirQualityVisible ->
            btnRemoveAirQuality.isVisible = isRemoveAirQualityVisible
        }
        viewModel.airQuality.observe(viewLifecycleOwner) { airQuality ->
            textCity.text = airQuality.primaryAddress
            textCountry.text = airQuality.secondaryAddress
            textIndex.text = airQuality.airQualityIndex

            textSulfurDioxideValue.text = formatText(R.string.template_μgm, airQuality.sulfurOxide)
            textPM25Value.text = formatText(R.string.template_μgm, airQuality.particle25)
            textPM10Value.text = formatText(R.string.template_μgm, airQuality.particle10)
            textOzoneValue.text = formatText(R.string.template_μgm, airQuality.ozone)
            textTimeRecordedValue.text = airQuality.localTimeRecorded.toString()
            pollutionView.setPollution(airQuality.airQualityIndex)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                air_quality_container.showError(
                    when (error) {
                        is Error.RefreshAirQuality -> getString(R.string.error_occurred)
                    }
                )
            }
        }
        viewModel.isProgressVisible.observe(viewLifecycleOwner) { isProgressVisible ->
            swipeToRefreshLayout.isRefreshing = isProgressVisible == true
        }
    }

    private fun formatText(templateResId: Int, value: Any?) =
        getString(templateResId, value?.toString() ?: "-")

    private fun showLocationNameDialog() {
//        viewModel.airQuality.value?.city?.name?.let {
//            AlertDialog.Builder(requireActivity(), R.style.Dialog).apply {
//                setTitle(R.string.dialog_title_location_name)
//                setMessage(it)
//                setPositiveButton(R.string.close) { dialog, _ -> dialog.dismiss() }
//            }.show()
//        }
    }

    private fun showExplanationDialog(@StringRes titleResId: Int, @StringRes messageResId: Int) {
        AlertDialog.Builder(requireActivity(), R.style.Dialog).apply {
            setTitle(titleResId)
            setMessage(messageResId)
            setPositiveButton(R.string.close) { dialog, _ -> dialog.dismiss() }
        }.show()
    }
}
