package lt.kepo.airq.api

import lt.kepo.airq.api.dto.AirQualityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {
    @GET("/feed/here/")
    suspend fun getAirQualityHere(): Response<ApiResponse<AirQualityDto>>

    @GET("/feed/{geo}/")
    suspend fun getAirQualityHereByLocation(@Path(value = "geo") geolocation: String): Response<ApiResponse<AirQualityDto>>
}