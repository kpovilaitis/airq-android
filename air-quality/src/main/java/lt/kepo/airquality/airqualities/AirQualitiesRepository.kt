package lt.kepo.airquality.airqualities

import kotlinx.coroutines.flow.Flow
import lt.kepo.airquality.AirQuality

interface AirQualitiesRepository {

    fun getAirQualities(): Flow<List<AirQuality>>

    suspend fun refresh(): RefreshResult

    sealed class RefreshResult {

        object Success : RefreshResult()

        object Error : RefreshResult()
    }
}
