package lt.kepo.airqualitynetwork

import lt.kepo.airqualitynetwork.response.AirQualityApiResponse
import lt.kepo.airqualitynetwork.response.AirQualityResponse
import lt.kepo.airqualitynetwork.response.StationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AirQualityApi {

    @GET("/feed/here/")
    suspend fun getAirQualityHere(
    ): Response<AirQualityApiResponse<AirQualityResponse>>

    @GET("/feed/geo:{latitude};{longitude}/")
    suspend fun getAirQualityHere(
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String
    ): Response<AirQualityApiResponse<AirQualityResponse>>

    @GET("/feed/@{stationId}/")
    suspend fun getAirQuality(
        @Path("stationId") stationId: Int
    ): Response<AirQualityApiResponse<AirQualityResponse>>

    @GET("/search/")
    suspend fun getStations(
        @Query("keyword") query: String
    ): Response<AirQualityApiResponse<List<StationResponse>>>
}