package lt.kepo.airq.api

import lt.kepo.airq.api.dto.AirQualityDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {
    @GET("/feed/here/")
    suspend fun getHere(): Response<ApiResponse<AirQualityDto>>
}