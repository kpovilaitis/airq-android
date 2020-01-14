package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun getRemoteAirQuality(stationId: Int): ApiResponse<AirQuality>

    fun getLocalAirQualities(): LiveData<List<AirQuality>>

    suspend fun insertLocalAirQuality(airQuality: AirQuality): Long

    fun getLocalAirQualityHere(): LiveData<AirQuality>

    suspend fun getLocalAirQualityHereId(): Int

    fun getLocalAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun deleteLocalAirQualityHere()

    suspend fun deleteLocalAirQuality(stationId: Int)
}