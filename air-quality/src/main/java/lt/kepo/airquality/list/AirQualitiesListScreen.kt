package lt.kepo.airquality.list

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
import lt.kepo.airquality.R
import lt.kepo.core.SimpleEvent

@Composable
fun AirQualitiesListScreen(
    navController: NavHostController,
    viewModel: AirQualitiesViewModel,
) {
    val airQualities by viewModel.airQualities.collectAsState(emptyList())
    val isProgressVisible by viewModel.isProgressVisible.collectAsState(false)
    val showError by viewModel.showError.collectAsState(null)

    AirQualitiesListScreen(
        airQualities = airQualities,
        isProgressVisible = isProgressVisible,
        showError = showError,
        onRefresh = {
            viewModel.refresh()
        },
        onAirQualityClick = { airQualityStationId ->
            navController.navigate(
                route = "details/$airQualityStationId",
            )
        },
        onAddAirQualityClick = {
            navController.navigate(
                route = "search",
            )
        },
    )
}

@Composable
private fun AirQualitiesListScreen(
    airQualities: List<AirQualitiesListItem>,
    isProgressVisible: Boolean,
    showError: SimpleEvent?,
    onRefresh: () -> Unit,
    onAirQualityClick: (Int) -> Unit,
    onAddAirQualityClick: () -> Unit,
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
                    .fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(
                                horizontal = 20.dp,
                                vertical = 12.dp,
                            ),
                    )

                    Divider(
                        color = MaterialTheme.colors.primary,
                        thickness = 2.dp,
                    )
                }
            }
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isProgressVisible),
                onRefresh = { onRefresh() },
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 8.dp
                    ),
                    modifier = Modifier.fillMaxHeight()
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
                    )
                }
            )
        },
    )
}

@Preview
@Composable
fun PreviewAirQualitiesScreen() {
    AirQualitiesListScreen(
        airQualities = listOf(
            AirQualitiesListItem(
                stationId = 1,
                address = "Kaunas, Lietuva",
                index = "12",
                isCurrentLocation = true
            )
        ),
        isProgressVisible = true,
        showError = null,
        onRefresh = { },
        onAirQualityClick = { },
        onAddAirQualityClick = { },
    )
}
