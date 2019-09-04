package lt.kepo.airq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import lt.kepo.airq.R
import lt.kepo.airq.viewmodels.AirQualityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AirQualityFragment : Fragment() {

    val viewModel: AirQualityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_air_quality, container, false)

        return view
    }
}