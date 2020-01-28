package lt.kepo.stations.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stations.*
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import lt.kepo.core.model.Station
import lt.kepo.core.ui.getListDivider
import lt.kepo.core.ui.showError
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

        findViewById<CoordinatorLayout>(R.id.container).systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        stationsAdapter = StationsAdapter(emptyList()) { viewModel.addStationAirQuality(it) }

        stationsRecyclerView.layoutManager = LinearLayoutManager(this)
        stationsRecyclerView.addItemDecoration(getListDivider(this, R.drawable.divider_stations))
        stationsRecyclerView.adapter = stationsAdapter
        stationsRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbar.isSelected = stationsRecyclerView.canScrollVertically(-1)
        }

        viewModel.errorMessage.observe(this, errorObserver)
        viewModel.isLoading.observe(this, progressObserver)
        viewModel.stations.observe(this, stationsObserver)

        search.setOnQueryTextListener(queryTextListener)
        search.requestFocus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                search.clearFocus()
                setResult(Activity.RESULT_OK)
                finish()
                overridePendingTransition(R.anim.window_pop_enter, R.anim.window_pop_exit)
                true
            }
            else -> false
        }
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
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

    private val errorObserver = Observer<String> { it?.let { error -> container.showError(error) } }

    private val stationsObserver = Observer<MutableList<Station>> { stations ->
        view_try_typing.isVisible = stations.isEmpty() && search.query.isEmpty()
        view_no_result.isVisible = stations.isEmpty() && search.query.isNotEmpty()
        stationsRecyclerView.isVisible = stations.isNotEmpty()

        if (stationsAdapter.stations.size - stations.size == 1) {

            val position = stationsAdapter.stations.indexOf(
                (stationsAdapter.stations + stations).groupBy { it.id }
                    .filter { it.value.size == 1 }
                    .flatMap { it.value }
                    .first()
            )

            stationsAdapter.stations = stations.toList()
            stationsAdapter.notifyItemRemoved(position)
        } else {
            stationsAdapter.stations = stations.toList()
            stationsAdapter.notifyDataSetChanged()
        }
    }
}