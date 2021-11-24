package lt.kepo.airq

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AirQTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (!useDarkTheme) {
        airQLightColors
    } else {
        airQDarkColors
    }

    MaterialTheme(
        colors = colors,
        typography = airQTypography,
    ) {
        content()
    }
}

private val airQDarkColors = darkColors(
    primary = Color(0xFFCECBCA),
    secondary = Color(0xFFCECBCA),
)

private val airQLightColors = lightColors(
    primary = Color(0xFF151414),
    secondary = Color(0xFF151414),
)
