package lt.kepo.airq.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stations.*
import lt.kepo.airq.R
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.ui.adapter.StationsAdapter
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import lt.kepo.airq.utility.RECYCLER_VIEW_SCROLL_DIRECTION_UP
import lt.kepo.airq.utility.getListDivider
import org.koin.androidx.viewmodel.ext.android.viewModel

class StationsActivity : AppCompatActivity() {
    private val viewModel: StationsViewModel by viewModel()

    private lateinit var stationsAdapter: StationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_stations)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        stationsAdapter = StationsAdapter(emptyList(), listClickListener)

        stationsRecyclerView.layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.addItemDecoration(getListDivider(this, R.drawable.divider_stations))
        stationsRecyclerView.adapter = stationsAdapter
        stationsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbar.isSelected = stationsRecyclerView.canScrollVertically(RECYCLER_VIEW_SCROLL_DIRECTION_UP)
        }

        viewModel.isLoading.observe(this, progressObserver)
        viewModel.stations.observe(this, stationsObserver)

        search.setOnQueryTextListener(queryTextListener)
        search.requestFocus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_OK)
                finish()
                true
            }
            else -> false
        }
    }

    private val listClickListener: (Station, Int) -> Unit = { station, position->
        viewModel.addStationAirQuality(station)
        viewModel.stations.value?.remove(station)
        stationsAdapter.notifyItemRemoved(position)
    }

    private val queryTextListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false

        override fun onQueryTextChange(newText: String): Boolean {
            if (newText.isEmpty())
                viewModel.clearStations()
            if (newText.length > 1)
                viewModel.getRemoteStations(newText)

            return true
        }
    }

    private val progressObserver = Observer<Boolean> { isLoading -> }

    private val stationsObserver = Observer<MutableList<Station>> { it?.let { stations ->
            stationsAdapter.stations = stations
            stationsAdapter.notifyDataSetChanged()
        }
    }
}