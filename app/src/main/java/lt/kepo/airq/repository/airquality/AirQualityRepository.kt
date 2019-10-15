package lt.kepo.airq.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.db.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun getRemoteAirQuality(stationId: Int): AirQuality

    fun getLocalAirQualityHere(): LiveData<AirQuality>

    suspend fun getLocalAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun upsertLocalAirQualityHere(airQuality: AirQuality)
}