package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun getRemoteAirQuality(stationId: Int): ApiResponse<AirQuality>

    suspend fun getLocalAirQualities(): List<AirQuality>

    suspend fun insertLocalAirQuality(airQuality: AirQuality): Long

    suspend fun deleteLocalAirQualityHere()

    suspend fun upsertLocalAirQuality(airQuality: AirQuality)
}