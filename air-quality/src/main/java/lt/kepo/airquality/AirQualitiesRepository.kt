package lt.kepo.airquality

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQuality

interface AirQualitiesRepository {

    fun getAll(): Flow<List<AirQuality>>

    fun getAirQuality(stationId: Int): Flow<AirQuality?>

    suspend fun remove(stationId: Int)

    suspend fun refresh(): RefreshResult

    sealed class RefreshResult {

        object Success : RefreshResult()

        object Error : RefreshResult()
    }
}
