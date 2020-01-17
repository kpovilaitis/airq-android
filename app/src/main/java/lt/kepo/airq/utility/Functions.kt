package lt.kepo.airq.utility

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import lt.kepo.airq.R

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

fun Fragment.showError(errorMessage: String, container: View) {
    val snack = Snackbar.make(container, errorMessage, Snackbar.LENGTH_LONG)
    val tv = snack.view.findViewById(R.id.snackbar_text) as TextView

    tv.setTextColor(resources.getColor(R.color.colorAccentTint, null))
    snack.view.setBackgroundResource(R.color.colorAccent)
    snack.show()
}