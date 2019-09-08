package lt.kepo.airq.api

import lt.kepo.airq.data.models.AirQuality
import retrofit2.http.GET

interface ApiClient {
    @GET("/feed/here/")
    suspend fun getHere(): ApiResponse<AirQuality>
}