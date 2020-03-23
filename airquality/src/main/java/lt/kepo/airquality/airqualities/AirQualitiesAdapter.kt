package lt.kepo.airquality.airqualities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_air_quality.view.*
import lt.kepo.airquality.R
import lt.kepo.core.constants.AIR_QUALITY_HERE_STATION_ID
import lt.kepo.core.ui.setFullName
import lt.kepo.core.ui.setPollution

class AirQualitiesAdapter(
    var airQualities: List<lt.kepo.core.model.AirQuality>,
    private val clickListener: (lt.kepo.core.model.AirQuality) -> Unit
) : RecyclerView.Adapter<AirQualitiesAdapter.ViewHolder>() {
    override fun getItemCount(): Int = airQualities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(airQualities[position], clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: lt.kepo.core.model.AirQuality, listener: (lt.kepo.core.model.AirQuality) -> Unit) = with(itemView) {
            setFullName(item.city.name, textCity, textCountry)
            currentLocationImageView.isVisible = item.stationId == AIR_QUALITY_HERE_STATION_ID

            textIndex.text = item.airQualityIndex

            itemView.pollutionView.setPollution(item.airQualityIndex)
            itemView.setOnClickListener { listener(item) }
        }
    }
}