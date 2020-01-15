package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun updateAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun updateAirQuality(stationId: Int): ApiResponse<AirQuality>

    fun getCachedAirQualities(): LiveData<List<AirQuality>>

    fun getCachedAirQualityHere(): LiveData<AirQuality>



    fun getCachedAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun deleteCachedAirQuality(stationId: Int)



    suspend fun getCachedAirQualityHereId(): Int

    suspend fun addAirQualityWithTimestamp(stationId: Int): ApiResponse<AirQuality>
}