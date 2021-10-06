package lt.kepo.airquality.list

import androidx.compose.runtime.Composable
import lt.kepo.uicore.IndexListItem

data class AirQualitiesListItem(
    val stationId: Int,
    val primaryAddress: String,
    val secondaryAddress: String,
    val airQualityIndex: String,
)

@Composable
fun AirQualitiesListItem(
    listItem: AirQualitiesListItem,
    onClick: (Int) -> Unit,
) {
    IndexListItem(
        index = listItem.airQualityIndex,
        title = listItem.primaryAddress,
        onClick = {
            onClick(listItem.stationId)
        },
    )
}
