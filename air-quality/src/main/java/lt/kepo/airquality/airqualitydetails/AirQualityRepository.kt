package lt.kepo.airquality.airqualitydetails

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQuality

interface AirQualityRepository {

    fun getAirQuality(
        stationId: Int
    ): Flow<AirQuality>

    suspend fun remove(stationId: Int)

    suspend fun refresh(
        stationId: Int
    ): RefreshResult

    sealed class RefreshResult {

        object Success : RefreshResult()

        object Error : RefreshResult()
    }
}
