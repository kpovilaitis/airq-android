package lt.kepo.airquality.airqualities

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import lt.kepo.airquality.R

@Composable
fun AirQualitiesListScreen(
    navController: NavHostController,
    viewModel: AirQualitiesViewModel,
) {
    val airQualities by viewModel.airQualities.collectAsState()
    val isProgressVisible by viewModel.isProgressVisible.collectAsState()
    val error by viewModel.error.collectAsState()

    AirQualitiesListScreen(
        airQualities = airQualities,
        isProgressVisible = isProgressVisible,
        errorMessage = error?.toMessage(),
        onRefresh = { isForced ->
            viewModel.refreshAirQualities(isForced)
        },
        onAirQualityClick = { airQualityStationId ->
            navController.navigate(
                route = "airQualityDetails/$airQualityStationId",
            )
        },
        onAddAirQualityClick = {
            navController.navigate(
                route = "stations",
            )
        },
    )
}

@Composable
private fun AirQualitiesListScreen(
    airQualities: List<AirQualitiesListItem>,
    isProgressVisible: Boolean,
    errorMessage: String?,
    onRefresh: (Boolean) -> Unit,
    onAirQualityClick: (Int) -> Unit,
    onAddAirQualityClick: () -> Unit,
) {
    LaunchedEffect("on_start_refresh") {
        onRefresh(false)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .padding(
                                horizontal = 20.dp,
                                vertical = 11.dp,
                            ),
                    )

                    Divider(
                        color = MaterialTheme.colors.onBackground,
                        thickness = 2.dp,
                    )
                }
            }
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isProgressVisible),
                onRefresh = { onRefresh(true) },
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 8.dp
                    ),
                ) {
                    items(airQualities) { airQualityListItem ->
                        AirQualitiesListItem(
                            listItem = airQualityListItem,
                        ) { stationId ->
                            onAirQualityClick(stationId)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddAirQualityClick()
                },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSecondary,
                    )
                }
            )
        },
        snackbarHost = { snackBarState ->
            errorMessage?.let { message ->
                rememberCoroutineScope().launch {
                    snackBarState.showSnackbar(
                        message = message,
                    )
                }
            }
        },
    )
}

@Composable
private fun AirQualitiesViewModel.Error.toMessage(): String =
    when (this) {
        is AirQualitiesViewModel.Error.RefreshAirQualities -> {
            stringResource(R.string.error_occurred)
        }
    }


@Preview
@Composable
fun PreviewAirQualitiesScreen() {
    AirQualitiesListScreen(
        airQualities = listOf(
            AirQualitiesListItem(
                stationId = 1,
                primaryAddress = "Kaunas",
                secondaryAddress = "Lietuva",
                airQualityIndex = "12",
            )
        ),
        isProgressVisible = true,
        errorMessage = "error",
        onRefresh = { },
        onAirQualityClick = { },
        onAddAirQualityClick = { },
    )
}
