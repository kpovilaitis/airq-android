package lt.kepo.airq.db.airquality

import androidx.lifecycle.LiveData
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.data.models.AirQuality

interface AirQualityRepository {
    suspend fun getByStationIdFromApi(stationId: Int): LiveData<AirQuality>

    suspend fun getHereFromApi(): ApiResponse<AirQuality>

    suspend fun getHereFromDb(): AirQuality

    suspend fun insert(airQuality: AirQuality)
}