package lt.kepo.uicore

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

@Composable
fun String.getAirQualityIndexColor(): Color =
    toIntOrNull()
        .let { index ->
            colorResource(
                when {
                    index == null -> {
                        R.color.colorPollutionUnknown
                    }
                    index < 51 -> {
                        R.color.colorPollutionGood
                    }
                    index in 51..100 -> {
                        R.color.colorPollutionModerate
                    }
                    index in 101..150 -> {
                        R.color.colorPollutionUnhealthyForSensitive
                    }
                    index in 151..200 -> {
                        R.color.colorPollutionUnhealthy
                    }
                    index in 201..300 -> {
                        R.color.colorPollutionVeryUnhealthy
                    }
                    index > 300 -> {
                        R.color.colorPollutionHazardous
                    }
                    else -> {
                        R.color.colorPollutionUnknown
                    }
                }
            )
        }

