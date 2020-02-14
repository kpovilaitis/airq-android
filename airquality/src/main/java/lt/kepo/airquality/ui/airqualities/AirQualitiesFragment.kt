package lt.kepo.airquality.ui.airqualities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import lt.kepo.airquality.R
import lt.kepo.airquality.ui.airquality.AirQualityFragment
import lt.kepo.core.ui.AppNavigator
import lt.kepo.core.model.AirQuality
import lt.kepo.core.ui.commitWithAnimations
import lt.kepo.core.ui.getListDivider
import lt.kepo.core.ui.showError
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirQualitiesFragment : Fragment() {
    private val navigator: AppNavigator by inject()
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

        fab.setOnClickListener { navigator.startStationsActivity(requireActivity()) }
//            animateFAB()
//        startActivity(Intent(requireContext(), StationsActivity::class.java))

//        fab1.setOnClickListener {
//            startActivityForResult(Intent(requireContext(), StationsActivity::class.java), STATIONS_ACTIVITY_REQUEST_CODE)
//            animateFAB()
//        }
//        fab2.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingsActivity::class.java))
//            animateFAB()
//        }

        swipeToRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)
        swipeToRefreshLayout.setColorSchemeResources(R.color.colorAccentTint)
        swipeToRefreshLayout.setOnRefreshListener { viewModel.updateCachedAirQualities(true) }

        adapter = AirQualitiesAdapter(emptyList(), listClickListener)
        airQualitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        airQualitiesRecyclerView.addItemDecoration(getListDivider(requireContext(), R.drawable.divider_air_qualities))
        airQualitiesRecyclerView.adapter = adapter

        airQualitiesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            textAppName.isSelected = airQualitiesRecyclerView.canScrollVertically(-1)
        }

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

        viewModel.updateCachedAirQualities()
    }

    private val listClickListener: (AirQuality) -> Unit = { airQuality ->
        val nextFragment = AirQualityFragment()
        val bundle = Bundle()

        bundle.putParcelable(AirQuality::class.java.simpleName, airQuality)

        nextFragment.arguments = bundle

        parentFragmentManager.commitWithAnimations{
            replace(R.id.main_content, nextFragment)
            addToBackStack(null)
        }
    }

    private val airQualitiesObserver = Observer<List<AirQuality>> { airQualities ->
        adapter.airQualities = airQualities
        adapter.notifyDataSetChanged()
    }

    private val errorMessageObserver = Observer<String> { it?.let { errorMessage -> container.showError(errorMessage) } }

    private val progressObserver = Observer<Boolean> { swipeToRefreshLayout.isRefreshing = it }
}