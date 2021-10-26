package lt.kepo.airquality.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lt.kepo.airquality.R
import lt.kepo.core.getAirQualityIndexColor

data class AirQualitiesListItem(
    val stationId: Int,
    val address: String,
    val index: String,
    val isCurrentLocation: Boolean,
)

@Composable
fun AirQualitiesListItem(
    listItem: AirQualitiesListItem,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable {
                onClick(listItem.stationId)
            }.fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val pollutionIndicatorColor by animateColorAsState(
            listItem.index.getAirQualityIndexColor()
        )

        Box(
            modifier = Modifier
                .padding(
                    end = 20.dp,
                )
                .clip(CircleShape)
                .background(pollutionIndicatorColor)
                .size(44.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = listItem.index,
                style = MaterialTheme.typography.h6,
            )
        }

        Text(
            text = listItem.address,
            maxLines = 1,
            style = MaterialTheme.typography.h6,
        )

        if (listItem.isCurrentLocation) {
            Spacer(
                modifier = Modifier
                    .weight(1.0f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                    )
            )
        }
    }
}

@Preview
@Composable
fun PreviewIndexListItem(
) {
    AirQualitiesListItem(
        listItem = AirQualitiesListItem(
            stationId = 1,
            address = "Kaunas",
            index = "12",
            isCurrentLocation = true
        ),
        onClick = {},
    )
}
