package lt.kepo.core.network

import lt.kepo.core.model.AirQuality
import lt.kepo.core.model.Station
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HttpClient {
    @GET("/feed/here/")
    suspend fun getAirQualityHere(): Response<ApiHttpResponse<AirQuality>>

    @GET("/feed/{geo}/")
    suspend fun getAirQualityHere(@Path("geo") geolocation: String): Response<ApiHttpResponse<AirQuality>>

    @GET("/feed/{stationId}/")
    suspend fun getAirQuality(@Path("stationId") stationId: String): Response<ApiHttpResponse<AirQuality>>

    @GET("/search/")
    suspend fun getStations(@Query("keyword") query: String): Response<ApiHttpResponse<MutableList<Station>>>
}