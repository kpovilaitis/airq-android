package lt.kepo.airq.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import kotlinx.android.synthetic.main.fragment_air_qualities.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.activity.StationsActivity
import lt.kepo.airq.ui.adapter.AirQualitiesAdapter
import lt.kepo.airq.ui.viewmodel.AirQualitiesViewModel
import lt.kepo.airq.utility.POSITION_PARCELABLE_KEY
import lt.kepo.airq.utility.getListDivider
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirQualitiesFragment : Fragment() {
    private val viewModel: AirQualitiesViewModel by viewModel()

    private var isFabOpen = false
    private lateinit var openAnimation: Animation
    private lateinit var closeAnimation: Animation

    private lateinit var stationsAdapter : AirQualitiesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_air_qualities, container, false)
    }

    override fun onViewCreated(@NonNull view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        closeAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)

        fab.setOnClickListener { animateFAB() }
        fab1.setOnClickListener {
            startActivity(Intent(requireContext(), StationsActivity::class.java))
            animateFAB()
        }
        fab2.setOnClickListener { animateFAB() }

        stationsAdapter = AirQualitiesAdapter(viewModel.airQualities.value, listClickListener)

        airQualitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        airQualitiesRecyclerView.addItemDecoration(getListDivider(requireContext()))
        airQualitiesRecyclerView.adapter = stationsAdapter

        viewModel.airQualities.observe(this, stationsObserver)
    }

    override fun onStart() {
        super.onStart()

        viewModel.getLocalAirQualities()
    }

    private fun animateFAB() {

        if (isFabOpen) {

            fab1.startAnimation(closeAnimation)
            fab2.startAnimation(closeAnimation)
            fab1.isClickable = false
            fab2.isClickable = false
            isFabOpen = false

        } else {

            fab1.startAnimation(openAnimation)
            fab2.startAnimation(openAnimation)
            fab1.isClickable = true
            fab2.isClickable = true
            isFabOpen = true
        }
    }

    private val listClickListener: (Int, View) -> Unit = { position, itemView ->
        val thisFragment = parentFragmentManager.findFragmentById(R.id.main_content)
        val nextFragment = AirQualityFragment()
        val bundle = Bundle()
        val city = itemView.findViewById<AppCompatTextView>(R.id.textCity)
        val country = itemView.findViewById<AppCompatTextView>(R.id.textCountry)
        val index = itemView.findViewById<AppCompatTextView>(R.id.textIndex)
        val action = itemView.findViewById<AppCompatImageView>(R.id.currentLocationImageView)

        bundle.putInt(POSITION_PARCELABLE_KEY, position)
        bundle.putParcelable(AirQuality::class.java.simpleName, viewModel.airQualities.value?.get(position))

        nextFragment.arguments = bundle

//        val exitFade = Fade()
//        exitFade.duration = 300
//        thisFragment?.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = 200
        nextFragment.sharedElementEnterTransition = enterTransitionSet

        // 3. Enter Transition for New Fragment
        val enterAnim = Slide()
        enterAnim.duration = 300
        nextFragment.enterTransition = enterAnim
        nextFragment.exitTransition = null

        val fragmentTransaction = parentFragmentManager.beginTransaction()

        fragmentTransaction.addSharedElement(city, city.transitionName)
        fragmentTransaction.addSharedElement(country, country.transitionName)
        fragmentTransaction.addSharedElement(index, index.transitionName)
        fragmentTransaction.addSharedElement(action, action.transitionName)

        fragmentTransaction.replace(R.id.main_content, nextFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private val stationsObserver = Observer<List<AirQuality>> { airQualities ->
        stationsAdapter.stations = airQualities
        stationsAdapter.notifyDataSetChanged()
    }
}