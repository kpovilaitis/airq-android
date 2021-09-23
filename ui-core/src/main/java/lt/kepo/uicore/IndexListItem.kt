package lt.kepo.uicore

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IndexListItem(
    index: String,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val pollutionIndicatorColor by animateColorAsState(
            index.getAirQualityIndexColor()
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
                text = index,
                style = MaterialTheme.typography.h6,
            )
        }

        Text(
            text = title,
//            fontSize = 18.sp,
            maxLines = 1,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Preview
@Composable
fun PreviewIndexListItem(
) {
    IndexListItem(
        title = "Kaunas",
        index = "12",
        onClick = {},
    )
}