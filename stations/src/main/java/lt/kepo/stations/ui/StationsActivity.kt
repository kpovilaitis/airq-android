package lt.kepo.stations.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stations.*
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import lt.kepo.core.model.Station
import lt.kepo.core.ui.getListDivider
import lt.kepo.stations.R
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

        stationsAdapter = StationsAdapter(emptyList()) { viewModel.addStationAirQuality(it) }

        stationsRecyclerView.layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.addItemDecoration(getListDivider(this, R.drawable.divider_stations))
        stationsRecyclerView.adapter = stationsAdapter
        stationsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbar.isSelected = stationsRecyclerView.canScrollVertically(-1)
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

    private val stationsObserver = Observer<MutableList<Station>> { list -> list?.let { stations ->
            if (stationsAdapter.stations.size - stations.size == 1) {

                val position = stationsAdapter.stations.indexOf(
                    (stationsAdapter.stations + stations)
                        .distinct()
                        .first()
                )

                stationsAdapter.stations = stations.toList()
                stationsAdapter.notifyItemRangeRemoved(position, 1)
            } else {
                stationsAdapter.stations = stations.toList()
                stationsAdapter.notifyDataSetChanged()
            }
        }
    }
}