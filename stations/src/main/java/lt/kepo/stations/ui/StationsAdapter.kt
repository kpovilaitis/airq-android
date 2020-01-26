package lt.kepo.stations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*
import lt.kepo.core.model.Station
import lt.kepo.stations.R

class StationsAdapter(
    var stations: List<Station>,
    private val clickListener: (Station) -> Unit
) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = stations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_station,
                    parent,
                    false
                )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(stations[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station, listener: (Station) -> Unit) = with(itemView) {
            itemView.progress_circular.alpha = 0.0f
            textStationName.text = item.station.name
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            setOnClickListener {
                itemView.progress_circular
                    .animate()
                    .alpha(1.0f)
                    .duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                listener(item)
            }
        }
    }
}