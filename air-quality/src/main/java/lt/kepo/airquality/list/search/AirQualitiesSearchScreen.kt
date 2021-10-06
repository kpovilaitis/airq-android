package lt.kepo.airquality.list.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import lt.kepo.airquality.R
import lt.kepo.airquality.list.AirQualitiesListItem

@Composable
fun AirQualitySearchScreen(
    navController: NavHostController,
    viewModel: SearchStationsViewModel,
) {
    val query by viewModel.query.collectAsState()
    val stations by viewModel.stations.collectAsState()
    val isClearActionVisible by viewModel.isClearActionVisible.collectAsState()

    AirQualitySearchScreen(
        query = query,
        airQualities = stations,
        isClearActionVisible = isClearActionVisible,
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
    onBackClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onQueryEntered: (String) -> Unit,
    onAirQualityClicked: (Int) -> Unit,
) {
    val requester = FocusRequester()

    LaunchedEffect("on_start_search") {
        requester.requestFocus()
    }

    Scaffold(
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
                    backgroundColor = MaterialTheme.colors.background,
//                    focusedIndicatorColor = MaterialTheme.colors.background,
//                    unfocusedIndicatorColor = MaterialTheme.colors.background,
                )
            )
        },
        content = {
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
                primaryAddress = "Some place far away",
                secondaryAddress = "Some place far away",
                airQualityIndex = "75",
            )
        ),
        isClearActionVisible = true,
        onBackClicked = { },
        onClearClicked = { },
        onQueryEntered = { },
        onAirQualityClicked = { },
    )
}