package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_air_quality.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.utility.setFullName
import lt.kepo.airq.utility.setPollution

class AirQualitiesAdapter(
    var airQualities: List<AirQuality>,
    private val clickListener: (AirQuality) -> Unit
) : RecyclerView.Adapter<AirQualitiesAdapter.ViewHolder>() {
    override fun getItemCount(): Int = airQualities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(airQualities[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AirQuality, listener: (AirQuality) -> Unit) = with(itemView) {
            setFullName(item.city.name, textCity, textCountry)
            currentLocationImageView.isVisible = item.isCurrentLocationQuality

            textIndex.text = item.airQualityIndex

            itemView.pollutionView.setPollution(item.airQualityIndex)
            itemView.setOnClickListener { listener(item) }
        }
    }
}