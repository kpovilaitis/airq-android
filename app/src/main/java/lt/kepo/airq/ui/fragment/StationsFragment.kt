package lt.kepo.airq.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search_stations.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.ui.activity.SearchStationsActivity
import lt.kepo.airq.ui.adapter.StationsAdapter
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StationsFragment : Fragment() {
    private val viewModel: StationsViewModel by viewModel()
    private lateinit var stationsAdapter : StationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_stations, container, false)
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationsAdapter = StationsAdapter(listClickListener, R.drawable.ic_delete)

        stationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        stationsRecyclerView.adapter = stationsAdapter

        viewModel.stations.observe(this, stationsObserver)
        viewModel.updateLocalStations()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = resources.getString(R.string.title_saved_stations)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getLocalAllStations()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.stations_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(requireContext(), SearchStationsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val listClickListener: (Int) -> Unit = { position ->
        viewModel.removeLocalStation(viewModel.stations.value!![position])
        viewModel.stations.value?.removeAt(position)
        stationsAdapter.notifyDataSetChanged()
    }

    private val stationsObserver = Observer<List<Station>> { stations ->
        stationsAdapter.submitList(stations)
    }
}