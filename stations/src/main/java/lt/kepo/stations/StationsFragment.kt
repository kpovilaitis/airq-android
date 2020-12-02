package lt.kepo.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stations.*
import lt.kepo.core.navigation.AppNavigator
import lt.kepo.core.ui.showError
import javax.inject.Inject

@AndroidEntryPoint
class StationsFragment : Fragment(R.layout.fragment_stations) {

    @Inject lateinit var navigator: AppNavigator
    private val viewModel: StationsViewModel by viewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setHasOptionsMenu(true)
//
//        setContentView(R.layout.fragment_stations)
//        setSupportActionBar(toolbar)
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//
//        findViewById<CoordinatorLayout>(R.id.container).systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationsAdapter = StationsAdapter { viewModel.addStation(it) }

        stationsRecyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.divider_stations
                    )?.let { drawable ->
                        setDrawable(drawable)
                    }
                }
        )
        stationsRecyclerView.adapter = stationsAdapter
        stationsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbar.isSelected = stationsRecyclerView.canScrollVertically(-1)
        }

        search.setOnQueryTextListener(queryTextListener)
        search.requestFocus()

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                stations_container.showError(
                    when (error) {
                        is StationsViewModel.Error.GetStations -> getString(R.string.error_get_stations)
                        is StationsViewModel.Error.AddStation -> getString(R.string.error_add_station)
                    }
                )
            }
        }
        viewModel.isProgressVisible.observe(viewLifecycleOwner) { isProgressVisible ->

        }
        viewModel.isTypingHintVisible.observe(viewLifecycleOwner) { isTypingHintVisible ->
            view_try_typing.isVisible = isTypingHintVisible
        }
        viewModel.isNoResultVisible.observe(viewLifecycleOwner) { isNoResultVisible ->
            view_no_result.isVisible = isNoResultVisible
        }
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            stationsAdapter.stations = stations
            stationsAdapter.notifyDataSetChanged()
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                search.clearFocus()
//                navigator.goBack()
//                true
//            }
//            else -> false
//        }
//    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false

        override fun onQueryTextChange(newText: String): Boolean {
            if (newText.isEmpty())
                viewModel.clearStations()
            if (newText.length > 1)
                viewModel.getStations(newText)

            return true
        }
    }
}
