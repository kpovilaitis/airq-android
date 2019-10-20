package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun getRemoteAirQuality(stationId: Int): AirQuality

    fun getLocalAirQualityHere(): LiveData<AirQuality>

    fun getLocalAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun upsertLocalAirQualityHere(airQuality: AirQuality)
}