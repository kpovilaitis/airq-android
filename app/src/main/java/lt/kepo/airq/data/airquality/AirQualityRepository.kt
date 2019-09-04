package lt.kepo.airq.data.airquality

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.data.models.AirQuality

interface AirQualityRepository {
    suspend fun getByStationId(stationId: Int): LiveData<AirQuality>

    suspend fun getHere(): ApiResponse<AirQuality>

//    suspend fun insert(airQuality: AirQuality)
}