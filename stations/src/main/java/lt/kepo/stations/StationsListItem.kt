package lt.kepo.stations

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import lt.kepo.uicore.IndexListItem

data class StationsListItem(
    val id: Int,
    val name: String,
    val airQualityIndex: String,
)

@Composable
fun StationsListItem(
    listItem: StationsListItem,
    onClick: (Int) -> Unit,
) {
    IndexListItem(
        index = listItem.airQualityIndex,
        title = listItem.name,
        onClick = {
            onClick(listItem.id)
        },
    )

}
