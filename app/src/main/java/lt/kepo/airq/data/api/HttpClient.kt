package lt.kepo.airq.data.api

import lt.kepo.airq.db.model.AirQuality
import lt.kepo.airq.db.model.Station
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HttpClient {
    @GET("/feed/here/")
    suspend fun getAirQualityHere(): Response<ApiHttpResponse<AirQuality>>

    @GET("/feed/{geo}/")
    suspend fun getAirQualityHere(@Path("geo") geolocation: String): Response<ApiHttpResponse<AirQuality>>

    @GET("/search/")
    suspend fun getStations(@Query("keyword") query: String): Response<ApiHttpResponse<List<Station>>>

    @GET("/feed/{stationId}/")
    suspend fun getStation(@Path("stationId") stationId: String): Response<ApiHttpResponse<AirQuality>>
}