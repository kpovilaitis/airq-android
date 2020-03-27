package lt.kepo.airquality.airqualities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_air_quality.view.*
import lt.kepo.airquality.AirQuality
import lt.kepo.airquality.R
import lt.kepo.airquality.setPollution

class AirQualitiesAdapter(
    var airQualities: List<AirQuality> = emptyList(),
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<AirQualitiesAdapter.ViewHolder>() {

    override fun getItemCount(): Int = airQualities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(airQualities[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AirQuality, listener: (Int) -> Unit) = with(itemView) {
            currentLocationImageView.isVisible = item.isCurrentLocationQuality

            textCity.text = item.primaryAddress
            textCountry.text = item.secondaryAddress
            textIndex.text = item.airQualityIndex

            itemView.pollutionView.setPollution(item.airQualityIndex)
            itemView.setOnClickListener {
                listener(item.stationId)
            }
        }
    }
}