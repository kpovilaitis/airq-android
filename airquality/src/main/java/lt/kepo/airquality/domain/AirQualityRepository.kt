package lt.kepo.airquality.domain

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.core.network.ApiResponse
import lt.kepo.core.model.AirQuality

interface AirQualityRepository {
    suspend fun updateAirQualityHere(location: Location?): ApiResponse<AirQuality>

    suspend fun updateAirQuality(stationId: Int): ApiResponse<AirQuality>

    fun getCachedAirQualitiesLive(): LiveData<List<AirQuality>>

    suspend fun getCachedAirQualities(): List<AirQuality>



    fun getCachedAirQuality(stationId: Int): LiveData<AirQuality>

    suspend fun deleteCachedAirQuality(stationId: Int)
}