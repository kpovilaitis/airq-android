package lt.kepo.airq.data.repository.airquality

import android.location.Location
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun getRemoteAirQuality(stationId: Int): ApiResponse<AirQuality>

    suspend fun getLocalAirQualities(): List<AirQuality>

    suspend fun insertLocalAirQuality(airQuality: AirQuality): Long

    suspend fun getLocalAirQualityHere(): AirQuality

    suspend fun deleteLocalAirQualityHere()

    suspend fun deleteLocalAirQuality(stationId: Int)

    suspend fun upsertLocalAirQuality(airQuality: AirQuality)
}