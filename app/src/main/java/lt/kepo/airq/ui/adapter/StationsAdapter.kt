package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.Station

class StationsAdapter(
    private val clickListener: (Int) -> Unit,
    private val actionDrawable: Int
) : ListAdapter<Station, StationsAdapter.ViewHolder>(StationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_station, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), position, clickListener, actionDrawable)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station, pos: Int, listener: (Int) -> Unit, actionDrawable: Int) = with(itemView) {
            textStationName.text = item.station.name
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            buttonAddStation.setImageResource(actionDrawable)
            buttonAddStation.setOnClickListener { listener(pos) }
        }
    }
}

private class StationDiffCallback : DiffUtil.ItemCallback<Station>() {

    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean = oldItem.airQualityIndex == newItem.airQualityIndex
}