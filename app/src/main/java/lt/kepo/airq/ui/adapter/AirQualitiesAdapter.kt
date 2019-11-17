package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_air_quality.view.*
import kotlinx.android.synthetic.main.view_air_quality_item.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.AirQuality

class AirQualitiesAdapter(
    var stations: List<AirQuality>?,
    private val clickListener: (Int, View) -> Unit
) : RecyclerView.Adapter<AirQualitiesAdapter.ViewHolder>() {
    override fun getItemCount(): Int = stations?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations?.get(position), position, clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AirQuality?, pos: Int, listener: (Int, View) -> Unit) = with(itemView) {
            val splitName = item?.city?.name?.split(",")?.toMutableList()
            val countryName = splitName?.removeAt(splitName.size - 1)
            val cityName = splitName?.joinToString()?.trimEnd()

            textCity.text = if (cityName?.isEmpty() == true) countryName else cityName
            textCountry.text = countryName?.trimStart()
            textIndex.text = item?.airQualityIndex

            textCity.transitionName = context.getString(R.string.transition_city_name) + pos
            textCountry.transitionName = context.getString(R.string.transition_country_name) + pos
            textIndex.transitionName = context.getString(R.string.transition_index) + pos
            currentLocationImageView.transitionName = context.getString(R.string.transition_action_icon) + pos

            if (item?.isCurrentLocationQuality == true)
                currentLocationImageView.visibility = View.VISIBLE
            else
                currentLocationImageView.visibility = View.GONE

            itemView.setOnClickListener { listener(pos, itemView) }
        }
    }
}