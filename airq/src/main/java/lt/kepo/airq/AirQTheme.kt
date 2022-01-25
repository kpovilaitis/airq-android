package lt.kepo.airq

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import lt.kepo.airquality.R

@Composable
fun AirQTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) {
        darkColors(
            primary = colorResource(R.color.colorPrimary),
            secondary = colorResource(R.color.colorSecondary),
        )
    } else {
        lightColors(
            primary = colorResource(R.color.colorPrimary),
            secondary = colorResource(R.color.colorSecondary),
        )
    }

    MaterialTheme(
        colors = colors,
        typography = airQTypography,
    ) {
        content()
    }
}
