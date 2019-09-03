package lt.kepo.airq.data.airquality

import androidx.lifecycle.LiveData
import lt.kepo.airq.data.models.AirQuality

interface AirQualityRepository {
    suspend fun getByStationId(stationId: Int): LiveData<AirQuality>

    suspend fun insert(airQuality: AirQuality)
}