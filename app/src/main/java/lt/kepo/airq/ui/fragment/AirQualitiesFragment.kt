package lt.kepo.airq.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import kotlinx.android.synthetic.main.fragment_air_qualities.container
import kotlinx.android.synthetic.main.fragment_air_qualities.swipeToRefreshLayout
import kotlinx.android.synthetic.main.list_item_air_quality.*
import kotlinx.android.synthetic.main.list_item_air_quality.textCity
import kotlinx.android.synthetic.main.list_item_air_quality.textCountry
import kotlinx.android.synthetic.main.list_item_air_quality.textIndex
import kotlinx.android.synthetic.main.list_item_air_quality.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.activity.StationsActivity
import lt.kepo.airq.ui.adapter.AirQualitiesAdapter
import lt.kepo.airq.ui.viewmodel.AirQualitiesViewModel
import lt.kepo.airq.utility.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AirQualitiesFragment : Fragment() {
    private val viewModel: AirQualitiesViewModel by viewModel()

    private lateinit var adapter: AirQualitiesAdapter

//    private var isFabOpen = false
//    private lateinit var openFabAnimation: Animation
//    private lateinit var closeFabAnimation: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_air_qualities, container, false)

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        openFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
//        closeFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)

        fab.setOnClickListener {
            startActivity(Intent(requireContext(), StationsActivity::class.java))
//            animateFAB()
        }
//        fab1.setOnClickListener {
//            startActivityForResult(Intent(requireContext(), StationsActivity::class.java), STATIONS_ACTIVITY_REQUEST_CODE)
//            animateFAB()
//        }
//        fab2.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingsActivity::class.java))
//            animateFAB()
//        }

        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.updateLocalAirQualityHere(true)
            viewModel.updateLocalAirQualities(true)
        }

        viewAirQualityHere.setOnClickListener { viewModel.airQualityHere.value?.let { listClickListener(it) } }

        adapter = AirQualitiesAdapter(emptyList(), listClickListener)
        airQualitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        airQualitiesRecyclerView.addItemDecoration(getListDivider(requireContext(), R.drawable.divider_air_qualities))
        airQualitiesRecyclerView.adapter = adapter

        scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            textAppName.isSelected = scrollView.canScrollVertically(RECYCLER_VIEW_SCROLL_DIRECTION_UP)
        }

        viewModel.airQualityHere.observe(viewLifecycleOwner, airQualityHereObserver)
        viewModel.airQualities.observe(viewLifecycleOwner, airQualitiesObserver)
        viewModel.errorMessage.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, progressObserver)
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

    override fun onStart() {
        super.onStart()

        viewModel.updateLocalAirQualities()
        viewModel.updateLocalAirQualityHere()
    }

    private val listClickListener: (AirQuality) -> Unit = { airQuality ->
        val nextFragment = AirQualityFragment()
        val bundle = Bundle()

        bundle.putParcelable(AirQuality::class.java.simpleName, airQuality)

        nextFragment.arguments = bundle

        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
            .replace(R.id.main_content, nextFragment)
            .addToBackStack(null)
            .commit()
    }

    private val airQualitiesObserver = Observer<List<AirQuality>> { it?.let { airQualities ->
            adapter.airQualities = airQualities
            adapter.notifyDataSetChanged()
        }
    }

    private val airQualityHereObserver = Observer<AirQuality> { it?.let { airQuality ->
            viewAirQualityHere.isVisible = true
            currentLocationImageView.visibility = View.VISIBLE
            setFullName(airQuality.city.name, textCity, textCountry)
            viewAirQualityHere.pollutionView.setPollution(airQuality.airQualityIndex)
            textIndex.text = airQuality.airQualityIndex
        }
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        val snack = Snackbar.make(container, errorMessage, Snackbar.LENGTH_LONG)
        val tv = snack.view.findViewById(R.id.snackbar_text) as TextView

        tv.setTextColor(resources.getColor(R.color.colorTextError, null))
        snack.show()
    }

    private val progressObserver = Observer<Boolean> { swipeToRefreshLayout.isRefreshing = it }
}