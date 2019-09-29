package lt.kepo.airq.repository.airquality

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import lt.kepo.airq.api.ApiResponse
import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.db.model.AirQuality

interface AirQualityRepository {
    suspend fun getRemoteAirQualityHere(): ApiResponse<AirQualityDto>

    suspend fun getRemoteAirQualityHere(location: Location): ApiResponse<AirQualityDto>

    suspend fun getRemoteAirQuality(stationId: Int): AirQuality

    suspend fun getLocalAirQualityHere(): AirQuality

    suspend fun getLocalAirQuality(stationId: Int): AirQuality

    suspend fun upsertAirQualityHere(airQuality: AirQuality)
}