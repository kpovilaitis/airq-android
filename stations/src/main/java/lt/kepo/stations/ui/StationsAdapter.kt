package lt.kepo.stations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*
import lt.kepo.stations.R

class StationsAdapter(
    var stations: List<lt.kepo.core.model.Station>,
    private val clickListener: (lt.kepo.core.model.Station, Int) -> Unit
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: lt.kepo.core.model.Station, listener: (lt.kepo.core.model.Station, Int) -> Unit) = with(itemView) {
            textStationName.text = item.station.name
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            buttonAddStation.setOnClickListener { listener(item, layoutPosition) }
        }
    }
}