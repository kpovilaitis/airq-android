package lt.kepo.airqualitydata

import kotlinx.coroutines.flow.Flow

interface AirQualitiesRepository {

    val error: Flow<Error>

    val isLoading: Flow<Boolean>

    val airQualities: Flow<List<AirQualityListItem>>

    fun getAirQuality(stationId: Int): Flow<AirQualityDetails?>

    suspend fun add(stationId: Int)

    suspend fun remove(stationId: Int)

    suspend fun refresh()

    sealed class Error  {

        object Add: Error()

        object Refresh: Error()
    }
}
