package lt.kepo.airq.utility

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

fun getListDivider(context: Context, drawableId : Int): DividerItemDecoration {
    val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

    divider.setDrawable(context.getDrawable(drawableId)!!)

    return divider
}

fun setFullName(locationName: String?, textCity: AppCompatTextView, textCountry: AppCompatTextView) {
    var bracelessLocationName = locationName?.replace("(", "")
    bracelessLocationName = bracelessLocationName?.replace(")", "")
    val splitName = bracelessLocationName?.split(",")?.toMutableList()
    val countryName = splitName?.removeAt(splitName.size - 1)
    val cityName = splitName?.joinToString()?.trimEnd()

    textCity.text = if (cityName?.isEmpty() == true) countryName else cityName
    textCountry.text = countryName?.trimStart()
}