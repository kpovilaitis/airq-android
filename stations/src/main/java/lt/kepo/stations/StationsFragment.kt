package lt.kepo.stations

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stations.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import lt.kepo.core.ui.showError

@AndroidEntryPoint
class StationsFragment : Fragment(R.layout.fragment_stations) {

    private val viewModel: StationsViewModel by viewModels()

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_back.setOnClickListener {
            findNavController().popBackStack()
        }
        search_input.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                callbackFlow {
                    var textWatcher: TextWatcher? = doAfterTextChanged { newText ->
                        offer(newText.toString())
                    }
                    awaitClose {
                        textWatcher = null
                    }
                }
                    .debounce(400L)
                    .collect { debouncedInput ->
                        viewModel.getStations(
                            query = debouncedInput,
                        )
                    }

            }
            requestFocus()
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
                it.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        button_clear_search_input.setOnClickListener {
            search_input.setText("")
            viewModel.clearSearch()
        }

        val stationsAdapter = StationsAdapter { viewModel.addStation(it) }

        stations_list.apply {
            itemAnimator
            addItemDecoration(
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
            adapter = stationsAdapter
            setOnScrollChangeListener { _, _, _, _, _ ->
                toolbar.isSelected = stations_list.canScrollVertically(-1)
            }
        }

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
        viewModel.isNothingFoundVisible.observe(viewLifecycleOwner) { isNoResultVisible ->
            view_no_result.isVisible = isNoResultVisible
        }
        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            stationsAdapter.submitList(stations)
        }
    }

    override fun onStop() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.run {
            hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }

        super.onStop()
    }
}
