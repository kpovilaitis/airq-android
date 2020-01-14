package lt.kepo.airq.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.activity.StationsActivity
import lt.kepo.airq.ui.adapter.AirQualitiesAdapter
import lt.kepo.airq.ui.viewmodel.AirQualitiesViewModel
import lt.kepo.airq.utility.POSITION_PARCELABLE_KEY
import lt.kepo.airq.utility.RECYCLER_VIEW_SCROLL_DIRECTION_UP
import lt.kepo.airq.utility.STATIONS_ACTIVITY_REQUEST_CODE
import lt.kepo.airq.utility.getListDivider
import org.koin.androidx.viewmodel.ext.android.viewModel


class AirQualitiesFragment : Fragment() {
    private val viewModel: AirQualitiesViewModel by viewModel()

//    private var isFabOpen = false
//    private lateinit var openFabAnimation: Animation
//    private lateinit var closeFabAnimation: Animation

    private lateinit var stationsAdapter : AirQualitiesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_air_qualities, container, false)
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stationsAdapter = AirQualitiesAdapter(viewModel.airQualities.value, listClickListener)

//        openFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
//        closeFabAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)

        fab.setOnClickListener {
            startActivityForResult(Intent(requireContext(), StationsActivity::class.java), STATIONS_ACTIVITY_REQUEST_CODE)
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
            swipeToRefreshLayout.isRefreshing = true
            viewModel.updateLocalAirQualities()
        }

        airQualitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        airQualitiesRecyclerView.addItemDecoration(getListDivider(requireContext(), R.drawable.divider_air_qualities))
        airQualitiesRecyclerView.adapter = stationsAdapter
        airQualitiesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            textAppName.isSelected = airQualitiesRecyclerView.canScrollVertically(RECYCLER_VIEW_SCROLL_DIRECTION_UP)
        }

        viewModel.airQualities.observe(this, airQualitiesObserver)
        viewModel.errorMessage.observe(this, errorMessageObserver)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getLocalAirQualities()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == STATIONS_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.getLocalAirQualities()
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

    private val listClickListener: (Int, View) -> Unit = { position, itemView ->
        val nextFragment = AirQualityFragment()
        val bundle = Bundle()
        val city = itemView.findViewById<AppCompatTextView>(R.id.textCity)
        val country = itemView.findViewById<AppCompatTextView>(R.id.textCountry)
        val index = itemView.findViewById<AppCompatTextView>(R.id.textIndex)

        bundle.putInt(POSITION_PARCELABLE_KEY, position)
        bundle.putParcelable(AirQuality::class.java.simpleName, viewModel.airQualities.value?.get(position))

        nextFragment.arguments = bundle

        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        nextFragment.sharedElementEnterTransition = enterTransitionSet

        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_exit)
            .addSharedElement(city, city.transitionName)
            .addSharedElement(country, country.transitionName)
            .addSharedElement(index, index.transitionName)
            .replace(R.id.main_content, nextFragment)
            .addToBackStack(null)
            .commit()
    }

    private val airQualitiesObserver = Observer<List<AirQuality>> { airQualities ->
        stationsAdapter.stations = airQualities
        swipeToRefreshLayout.isRefreshing = false
        stationsAdapter.notifyDataSetChanged()
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        val snack = Snackbar.make(container, errorMessage, Snackbar.LENGTH_LONG)
        val tv = snack.view.findViewById(R.id.snackbar_text) as TextView

        tv.setTextColor(resources.getColor(R.color.colorTextError, null))
        snack.show()

        swipeToRefreshLayout.isRefreshing = false
    }
}