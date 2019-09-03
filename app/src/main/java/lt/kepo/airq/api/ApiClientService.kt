package lt.kepo.airq.api

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import lt.kepo.airq.data.models.AirQuality
import retrofit2.http.GET

interface ApiClientService {
    @GET("")
    fun getAirQualityHere(): Deferred<LiveData<ApiResponse<AirQuality>>>
}