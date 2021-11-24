package lt.kepo.airquality.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import lt.kepo.airquality.R
import lt.kepo.airquality.getAirQualityIndexColor
import lt.kepo.core.SimpleEvent

@Composable
fun AirQualityDetailsScreen(
    navController: NavController,
    viewModel: AirQualityDetailsViewModel,
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        val stationName by viewModel.stationName.collectAsState("")
        val index by viewModel.index.collectAsState("")
        val sulfurOxide by viewModel.sulfurOxide.collectAsState(null)
        val ozone by viewModel.ozone.collectAsState(null)
        val particle10 by viewModel.particle10.collectAsState(null)
        val particle25 by viewModel.particle25.collectAsState(null)
        val isCurrentLocationLabelVisible by viewModel.isCurrentLocationLabelVisible.collectAsState(false)
        val isRemoveAirQualityVisible by viewModel.isRemoveAirQualityVisible.collectAsState(false)
        val isProgressVisible by viewModel.isProgressVisible.collectAsState(false)
        val showError by viewModel.showError.collectAsState(null)

        AirQualityDetailsScreen(
            stationName = stationName,
            airQualityIndex = index,
            sulfurOxide = sulfurOxide,
            ozone = ozone,
            particle10 = particle10,
            particle25 = particle25,
            isCurrentLocationLabelVisible = isCurrentLocationLabelVisible,
            isRemoveVisible = isRemoveAirQualityVisible,
            isProgressVisible = isProgressVisible,
            showError = showError,
            onRefresh = {
                viewModel.refresh()
            },
            onBackClick = {
                navController.popBackStack()
            },
            onDeleteClick = {
                viewModel.remove()
                navController.popBackStack()
            }
        )
    }
}

@Composable
private fun AirQualityDetailsScreen(
    stationName: String,
    airQualityIndex: String,
    sulfurOxide: Double?,
    ozone: Double?,
    particle10: Double?,
    particle25: Double?,
    isCurrentLocationLabelVisible: Boolean,
    isRemoveVisible: Boolean,
    isProgressVisible: Boolean,
    showError: SimpleEvent?,
    onRefresh: () -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    if (showError?.notHandledContent != null) {
        val message = stringResource(R.string.error_occurred)
        LaunchedEffect("snackbar") {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp,
                    ),
            ) {
                IconButton(
                    onClick = { onBackClick() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }

                Spacer(
                    modifier = Modifier
                        .weight(1.0f)
                )

                if (isCurrentLocationLabelVisible) {
                    Icon(
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                if (isRemoveVisible) {
                    IconButton(
                        onClick = { onDeleteClick() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }
            }
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isProgressVisible),
                onRefresh = { onRefresh() },
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    val pollutionIndicatorColor by animateColorAsState(
                        airQualityIndex.getAirQualityIndexColor()
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(pollutionIndicatorColor)
                            .size(132.dp)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = airQualityIndex,
                            style = MaterialTheme.typography.h2,
                        )
                    }

                    Text(
                        text = stationName,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(20.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .height(40.dp)
                    )

                    ParticleItem(
                        label = stringResource(R.string.label_pm_25),
                        value = particle25?.toString(),
                        measurement = stringResource(R.string.template_ppb)
                    )

                    ParticleItem(
                        label = stringResource(R.string.label_pm_10),
                        value = particle10?.toString(),
                        measurement = stringResource(R.string.template_ppb)
                    )

                    ParticleItem(
                        label = stringResource(R.string.label_ozone),
                        value = ozone?.toString(),
                        measurement = stringResource(R.string.template_μgm)
                    )

                    ParticleItem(
                        label = stringResource(R.string.label_sulfur_dioxide),
                        value = sulfurOxide?.toString(),
                        measurement = stringResource(R.string.template_μgm)
                    )
                }
            }
        },
    )
}

@Composable
private fun ParticleItem(
    label: String,
    value: String?,
    measurement: String,
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 12.dp,
                horizontal = 48.dp,
            )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h6,
        )

        Spacer(
            modifier = Modifier
                .weight(1.0f)
        )

        Text(
            text = "${value ?: "-"} $measurement",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h6,
        )
    }
}


@Preview
@Composable
fun PreviewAirQualityDetailsScreen() {
    AirQualityDetailsScreen(
        stationName = "Kaunas",
        airQualityIndex = "100",
        sulfurOxide = 10.0,
        ozone = 10.0,
        particle10 = 10.0,
        particle25 = 10.0,
        isCurrentLocationLabelVisible = true,
        isRemoveVisible = false,
        isProgressVisible = false,
        showError = null,
        onRefresh = { },
        onBackClick = { },
        onDeleteClick = { },
    )
}
