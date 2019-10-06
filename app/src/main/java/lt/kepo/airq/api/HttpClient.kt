package lt.kepo.airq.api

import lt.kepo.airq.api.dto.AirQualityDto
import lt.kepo.airq.api.dto.StationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HttpClient {
    @GET("/feed/here/")
    suspend fun getAirQualityHere(): Response<ApiHttpResponse<AirQualityDto>>

    @GET("/feed/{geo}/")
    suspend fun getAirQualityHere(@Path("geo") geolocation: String): Response<ApiHttpResponse<AirQualityDto>>

    @GET("/search/")
    suspend fun getStations(@Query("keyword") query: String): Response<ApiHttpResponse<List<StationDto>>>

    @GET("/feed/{stationId}/")
    suspend fun getStation(@Path("stationId") stationId: String): Response<ApiHttpResponse<AirQualityDto>>
}