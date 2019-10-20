package lt.kepo.airq.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search_stations.*
import lt.kepo.airq.R
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lt.kepo.airq.ui.adapter.StationsAdapter
import lt.kepo.airq.ui.viewmodel.StationsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchStationsActivity : AppCompatActivity() {
    private val viewModel: StationsViewModel by viewModel()
    private lateinit var stationsAdapter: StationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_stations)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        stationsAdapter = StationsAdapter(listClickListener, R.drawable.ic_add)
        stationsAdapter.registerAdapterDataObserver(autocompleteListObserver)

        stationsRecyclerView.layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.adapter = stationsAdapter

        viewModel.stations.observe(this, Observer { stations -> stationsAdapter.submitList(stations) })

        search.setOnQueryTextListener(queryTextListener)
        search.requestFocus()
    }

    private val listClickListener: (Int) -> Unit = { position ->
        viewModel.addLocalStation(viewModel.stations.value!![position])
        viewModel.stations.value?.removeAt(position)
        stationsAdapter.notifyDataSetChanged()
    }

    private val autocompleteListObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) { scrollToTop() }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) { scrollToTop() }

        private fun scrollToTop() { (stationsRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0) }
    }

    private val queryTextListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false

        override fun onQueryTextChange(newText: String): Boolean {
            if (newText.length > 1)
                viewModel.getRemoteStations(newText)

            return true
        }
    }

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