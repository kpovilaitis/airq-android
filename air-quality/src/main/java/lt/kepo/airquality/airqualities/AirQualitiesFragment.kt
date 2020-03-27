package lt.kepo.airquality.airqualities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import kotlinx.android.synthetic.main.fragment_air_qualities.swipeToRefreshLayout
import lt.kepo.airquality.R
import lt.kepo.core.navigation.AppNavigator
import lt.kepo.core.ui.showError
import javax.inject.Inject

@AndroidEntryPoint
class AirQualitiesFragment : Fragment(R.layout.fragment_air_qualities) {

    @Inject lateinit var navigator: AppNavigator
    private val viewModel: AirQualitiesViewModel by viewModels()

    private lateinit var adapter: AirQualitiesAdapter

//    private var isFabOpen = false
//    private lateinit var openFabAnimation: Animation
//    private lateinit var closeFabAnimation: Animation

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        openFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
//        closeFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)

        air_qualities_container.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        fab.setOnClickListener {
            navigator.showStations()
        }
//            animateFAB()
//        startActivity(Intent(requireContext(), StationsActivity::class.java))

//        fab1.setOnClickListener {
//            startActivityForResult(Intent(requireContext(), StationsActivity::class.java), STATIONS_ACTIVITY_REQUEST_CODE)
//            animateFAB()
//        }
//        fab2.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingsActivity::class.java))
//            animateFAB()
//        }

        swipeToRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeToRefreshLayout.setColorSchemeResources(R.color.colorAccentTint)
        swipeToRefreshLayout.setOnRefreshListener { viewModel.refreshAirQualities(true) }

        adapter = AirQualitiesAdapter { navigator.showAirQualityDetails(it) }
        airQualitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        airQualitiesRecyclerView.adapter = adapter

        airQualitiesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            textAppName.isSelected = airQualitiesRecyclerView.canScrollVertically(-1)
        }

        viewModel.airQualities.observe(viewLifecycleOwner) { airQualities ->
            adapter.airQualities = airQualities
            adapter.notifyDataSetChanged()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            when (error) {
                is AirQualitiesViewModel.Error.RefreshAirQualities -> {
                    air_qualities_container.showError(error.message)
                }
            }
        }
        viewModel.isProgressVisible.observe(viewLifecycleOwner) { isProgressVisible ->
            swipeToRefreshLayout.isRefreshing = isProgressVisible == true
        }
    }

//    private fun animateFAB() {
//
//        if (isFabOpen) {
//            fab.setImageResource(R.drawable.ic_menu)
//            fab1.startAnimation(closeFabAnimation)
//            fab2.startAnimation(closeFabAnimation)
//            fab1.isClickable = false
//            fab2.isClickable = false
//            isFabOpen = false
//        } else {
//            fab.setImageResource(R.drawable.ic_remove)
//            fab1.startAnimation(openFabAnimation)
//            fab2.startAnimation(openFabAnimation)
//            fab1.isClickable = true
//            fab2.isClickable = true
//            isFabOpen = true
//        }
//    }
}