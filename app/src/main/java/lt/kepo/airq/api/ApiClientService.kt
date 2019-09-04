package lt.kepo.airq.api

import kotlinx.coroutines.Deferred
import lt.kepo.airq.data.models.AirQuality
import retrofit2.http.GET

interface ApiClientService {
    @GET("")
    fun getAirQualityHere(): Deferred<ApiResponse<AirQuality>>
}