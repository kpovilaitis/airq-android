package lt.kepo.airq.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search_stations.*
import lt.kepo.airq.R
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import lt.kepo.airq.ui.adapter.StationsAdapter
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchStationsActivity : AppCompatActivity() {
    private val viewModel: StationsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_stations)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val stationsAdapter = StationsAdapter(ArrayList(), listClickListener, R.drawable.ic_add)

        stationsRecyclerView.layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.adapter = stationsAdapter

        viewModel.stations.observe(this, Observer { stations ->
            stationsAdapter.stations = stations
            stationsAdapter.notifyDataSetChanged()
        })

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 1)
                    viewModel.getRemoteStations(newText)

                return true
            }
        })
    }

    private val listClickListener: (Int) -> Unit = { position -> viewModel.addStationToLocalStorage(viewModel.stations.value!![position]) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }
}