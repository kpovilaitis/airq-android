package lt.kepo.airquality.list.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import lt.kepo.airquality.R
import lt.kepo.airquality.list.AirQualitiesListItem
import lt.kepo.core.Event
import lt.kepo.core.collectAsStateWithLifecycle

@Composable
fun AirQualitySearchScreen(
    navController: NavHostController,
    viewModel: SearchStationsViewModel,
) {
    val query by viewModel.query.collectAsStateWithLifecycle("")
    val stations by viewModel.stations.collectAsStateWithLifecycle(emptyList())
    val isClearActionVisible by viewModel.isClearActionVisible.collectAsStateWithLifecycle(false)
    val isNoResultVisible by viewModel.isNoResultsVisible.collectAsStateWithLifecycle(false)
    val isProgressVisible by viewModel.isProgressVisible.collectAsStateWithLifecycle(false)
    val showError by viewModel.showError.collectAsStateWithLifecycle(null)

    AirQualitySearchScreen(
        query = query,
        airQualities = stations,
        isClearActionVisible = isClearActionVisible,
        isProgressVisible = isProgressVisible,
        isNoResultVisible = isNoResultVisible,
        showError = showError,
        onBackClicked = navController::popBackStack,
        onClearClicked = viewModel::clearSearch,
        onQueryEntered = { newText ->
            viewModel.onQueryEntered(
                query = newText,
            )
        },
        onAirQualityClicked = { stationId ->
            viewModel.add(
                stationId = stationId,
            )
        },
    )
}

@Composable
private fun AirQualitySearchScreen(
    query: String,
    airQualities: List<AirQualitiesListItem>,
    isClearActionVisible: Boolean,
    isProgressVisible: Boolean,
    isNoResultVisible: Boolean,
    showError: Event<SearchStationsViewModel.Error>?,
    onBackClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onQueryEntered: (String) -> Unit,
    onAirQualityClicked: (Int) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val requester = FocusRequester()

    LaunchedEffect("on_start_search") {
        requester.requestFocus()
    }

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
            TextField(
                value = query,
                onValueChange = { newText ->
                    onQueryEntered(newText)
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.hint_search),
                        style = MaterialTheme.typography.h6,
                    )
                },
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                trailingIcon ={
                    if (isClearActionVisible) {
                        IconButton(
                            onClick = { onClearClicked() }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clear),
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(requester),
                textStyle = MaterialTheme.typography.h6,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.background,
                )
            )
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isProgressVisible),
                swipeEnabled = false,
                onRefresh = { },
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
                            onAirQualityClicked(stationId)
                        }
                    }
                }

                if (isNoResultVisible) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.label_no_results),
                            style = MaterialTheme.typography.h6,

                            )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AirQualitySearchScreenPreview() {
    AirQualitySearchScreen(
        query = "Input",
        airQualities = listOf(
            AirQualitiesListItem(
                stationId = 1,
                address = "Some place far away",
                index = "75",
                isCurrentLocation = false,
            )
        ),
        isClearActionVisible = true,
        isProgressVisible = false,
        isNoResultVisible = true,
        showError = null,
        onBackClicked = { },
        onClearClicked = { },
        onQueryEntered = { },
        onAirQualityClicked = { },
    )
}