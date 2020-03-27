package lt.kepo.core.ui

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import lt.kepo.core.R

fun View.showError(errorMessage: String) {
    Snackbar.make(
        this,
        errorMessage,
        Snackbar.LENGTH_LONG
    ).apply {
        view.setBackgroundResource(R.color.colorAccent)
        view.findViewById<TextView>(R.id.snackbar_text)
            ?.setTextColor(resources.getColor(R.color.colorAccentTint, null))
    }.show()
}