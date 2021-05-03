package lt.kepo.airquality.airqualities

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import kotlinx.android.synthetic.main.fragment_air_qualities.swipeToRefreshLayout
import lt.kepo.airquality.R
import lt.kepo.core.ui.showError

@AndroidEntryPoint
class AirQualitiesFragment : Fragment(R.layout.fragment_air_qualities) {

    private val viewModel: AirQualitiesViewModel by viewModels()

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        air_qualities_container.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        fab.setOnClickListener {
            findNavController().navigate(
                AirQualitiesFragmentDirections.toStations()
            )
        }

        swipeToRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeToRefreshLayout.setColorSchemeResources(R.color.colorAccentTint)
        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.refreshAirQualities(
                isForced = true
            )
        }

        val adapter = AirQualitiesAdapter { stationId ->
            view.findNavController().navigate(
                AirQualitiesFragmentDirections.toAirQualityDetails(
                    stationId = stationId,
                )
            )
        }
        airQualitiesRecyclerView.adapter = adapter
        airQualitiesRecyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.divider_air_qualities
                    )?.let { drawable ->
                        setDrawable(drawable)
                    }
                }
        )

        airQualitiesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            textAppName.isSelected = airQualitiesRecyclerView.canScrollVertically(-1)
        }

        viewModel.airQualities.observe(viewLifecycleOwner) { airQualities ->
            adapter.airQualities = airQualities
            adapter.notifyDataSetChanged()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                air_qualities_container.showError(
                    when (error) {
                        is AirQualitiesViewModel.Error.RefreshAirQualities -> getString(R.string.error_occurred)
                    }
                )
            }
        }
        viewModel.isProgressVisible.observe(viewLifecycleOwner) { isProgressVisible ->
            swipeToRefreshLayout.isRefreshing = isProgressVisible
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.refreshAirQualities(
            isForced = false
        )
    }
}