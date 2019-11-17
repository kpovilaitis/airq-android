package lt.kepo.airq.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_air_quality_item.view.*
import lt.kepo.airq.R
import lt.kepo.airq.data.model.Station

class StationsAdapter(
    var stations: List<Station>?,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<StationsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = stations?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_air_quality, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stations?.get(position), position, clickListener)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Station?, pos: Int, listener: (Int) -> Unit) = with(itemView) {
            val splitName = item?.station?.name?.split(",")?.toMutableList()
            val countryName = splitName?.removeAt(splitName.size - 1)

            textCity.text = splitName?.joinToString()?.trimEnd()
            textCountry.text = countryName?.trimStart()
            textIndex.text = item?.airQualityIndex

            itemView.setOnLongClickListener { listener(pos); true}
        }
    }
}