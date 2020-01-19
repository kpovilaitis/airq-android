package lt.kepo.airq.data.repository.airquality

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.airq.data.api.ApiResponse
import lt.kepo.airq.data.model.AirQuality

interface AirQualityRepository {
    suspend fun updateAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun updateAirQuality(stationId: Int): ApiResponse<AirQuality>

    fun getCachedAirQualitiesLive(): LiveData<List<AirQuality>>

    suspend fun getCachedAirQualities(): List<AirQuality>



    fun getCachedAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun deleteCachedAirQuality(stationId: Int)
}