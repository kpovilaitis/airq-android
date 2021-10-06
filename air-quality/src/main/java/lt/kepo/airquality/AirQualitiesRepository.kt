package lt.kepo.airquality

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQuality

interface AirQualitiesRepository {

    val isRefreshing: Flow<Boolean>

    val airQualities: Flow<List<AirQuality>>

    fun getAirQuality(stationId: Int): Flow<AirQuality?>

    suspend fun remove(stationId: Int)

    suspend fun refresh()
//
//    sealed class RefreshResult {
//
//        object Success : RefreshResult()
//
//        object Error : RefreshResult()
//    }
}
