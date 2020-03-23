package lt.kepo.airquality.domain

import android.location.Location
import androidx.lifecycle.LiveData
import lt.kepo.core.network.ApiResponse
import lt.kepo.core.model.AirQuality

interface AirQualityRepository {
    suspend fun updateAirQualityHere(location: Location?): lt.kepo.core.network.ApiResponse<lt.kepo.core.model.AirQuality>

    suspend fun updateAirQuality(stationId: Int): lt.kepo.core.network.ApiResponse<lt.kepo.core.model.AirQuality>

    fun getCachedAirQualitiesLive(): LiveData<List<lt.kepo.core.model.AirQuality>>

    suspend fun getCachedAirQualities(): List<lt.kepo.core.model.AirQuality>



    fun getCachedAirQuality(stationId: Int): LiveData<lt.kepo.core.model.AirQuality>

    suspend fun deleteCachedAirQuality(stationId: Int)
}