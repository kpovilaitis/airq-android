package lt.kepo.airqualitydata

import kotlinx.coroutines.flow.Flow

interface AirQualitiesRepository {

    val loadState: Flow<LoadState>

    val airQualities: Flow<List<AirQualityListItem>>

    fun getAirQuality(stationId: Int): Flow<AirQualityDetails?>

    suspend fun add(stationId: Int)

    suspend fun remove(stationId: Int)

    suspend fun refresh()

    sealed class LoadState {

        object NotLoading: LoadState()

        object Loading: LoadState()

        sealed class Error: LoadState() {

            object Add: LoadState()

            object Refresh: LoadState()
        }
    }
}
