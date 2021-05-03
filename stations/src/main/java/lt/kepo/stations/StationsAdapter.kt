package lt.kepo.stations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_station.view.*

class StationsAdapter(
    private val clickListener: (Int) -> Unit,
) : ListAdapter<Station, StationsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_station,
                    parent,
                    false
                )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station, listener: (Int) -> Unit) = with(itemView) {
            itemView.progress_circular.alpha = 0.0f
            textStationName.text = item.name
            textIndex.text = item.airQualityIndex
            textStationAirQuality.text = resources.getString(R.string.label_station_air_quality, item.airQualityIndex)
            setOnClickListener {
                itemView.progress_circular
                    .animate()
                    .alpha(1.0f)
                    .duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                listener(item.id)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Station>() {

        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem == newItem
    }
}