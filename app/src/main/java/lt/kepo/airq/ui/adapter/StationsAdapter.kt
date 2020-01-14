package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.Station

class StationsAdapter(
    var stations: List<Station>,
    private val clickListener: (Station, Int) -> Unit
) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = stations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_station, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station, listener: (Station, Int) -> Unit) = with(itemView) {
            textStationName.text = item.station.name
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            buttonAddStation.setOnClickListener { listener(item, layoutPosition) }
        }
    }
}