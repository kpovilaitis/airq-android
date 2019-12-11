package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*
import kotlinx.android.synthetic.main.view_air_quality_item.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.Station
import lt.kepo.airq.ui.view.AirQualityView
import lt.kepo.airq.utility.setPollution

class StationsAdapter(
    var stations: List<Station>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        fun bind(item: Station?, pos: Int, listener: (Int) -> Unit) = with(itemView) {
//            setFullName(item?.station?.name, textCity, textCountry)
//
//            if (item?.airQualityIndex?.equals("-") == false)
//                itemView.findViewById<AirQualityView>(R.id.stationView).setPollution(item.airQualityIndex.toInt())
//
//            textIndex.text = item?.airQualityIndex
//
//            itemView.setOnLongClickListener { listener(pos); true}
//        }
//    }

    override fun getItemCount(): Int = stations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_station, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station, listener: (Int) -> Unit) = with(itemView) {
            textStationName.text = item.station.name
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            buttonAddStation.setOnClickListener { listener(layoutPosition) }
        }
    }
}