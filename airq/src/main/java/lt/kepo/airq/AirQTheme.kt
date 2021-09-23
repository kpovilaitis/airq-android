package lt.kepo.airq

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val air_q_white = Color.White
private val air_q_black = Color(0xFF1f202e)

val airQColors = lightColors(
    primary = air_q_black,
    secondary = air_q_black,
    onSecondary = air_q_white,
    surface = air_q_white,
    onSurface = air_q_black,
    onBackground = Color(0xFF111d22)
)

private val light = Font(R.font.montserrat_light, FontWeight.W300)
private val regular = Font(R.font.montserrat_regular, FontWeight.W400)
private val medium = Font(R.font.montserrat_medium, FontWeight.W500)
private val semibold = Font(R.font.montserrat_semibold, FontWeight.W600)

private val airQFontFamily = FontFamily(
    fonts = listOf(
        light,
        regular,
        medium,
        semibold
    )
)

val captionTextStyle = TextStyle(
    fontFamily = airQFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp
)

val airQTypography = Typography(
    h1 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 34.sp
    ),
    h5 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = airQFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)

@Composable
fun AirQTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = airQColors,
        typography = airQTypography,
    ) {
        content()
    }
}
