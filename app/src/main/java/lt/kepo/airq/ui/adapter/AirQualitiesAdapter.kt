package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_air_quality_item.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.ui.view.AirQualityItemView
import lt.kepo.airq.utility.setFullName

class AirQualitiesAdapter(
    var stations: List<AirQuality>?,
    private val clickListener: (Int, View) -> Unit
) : RecyclerView.Adapter<AirQualitiesAdapter.ViewHolder>() {
    override fun getItemCount(): Int = stations?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations?.get(position), position, clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AirQuality?, pos: Int, listener: (Int, View) -> Unit) = with(itemView) {
            setFullName(item?.city?.name, textCity, textCountry)

            if (item?.airQualityIndex?.equals("-") == false)
                itemView.findViewById<AirQualityItemView>(R.id.stationView).setPollution(item.airQualityIndex.toInt())

            textIndex.text = item?.airQualityIndex

            textCity.transitionName = context.getString(R.string.transition_city_name) + pos
            textCountry.transitionName = context.getString(R.string.transition_country_name) + pos
            textIndex.transitionName = context.getString(R.string.transition_index) + pos

            if (item?.isCurrentLocationQuality == true)
                currentLocationImageView.visibility = View.VISIBLE
            else
                currentLocationImageView.visibility = View.GONE

            itemView.setOnClickListener { listener(pos, itemView) }
        }
    }
}