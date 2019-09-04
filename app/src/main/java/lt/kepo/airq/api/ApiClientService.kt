package lt.kepo.airq.api

import lt.kepo.airq.data.models.AirQuality
import retrofit2.http.GET

interface ApiClientService {
    @GET("/feed/here/")
    suspend fun getAirQualityHere(): ApiResponse<AirQuality>
}