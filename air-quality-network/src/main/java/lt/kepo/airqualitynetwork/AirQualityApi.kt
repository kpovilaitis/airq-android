package lt.kepo.airqualitynetwork

import lt.kepo.airqualitynetwork.response.AirQualityResponse
import lt.kepo.airqualitynetwork.response.StationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AirQualityApi {

    @GET("/feed/here/")
    suspend fun getAirQualityHere(
    ): ApiResult<AirQualityResponse>

    @GET("/feed/geo:{latitude};{longitude}/")
    suspend fun getAirQualityHere(
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String
    ): ApiResult<AirQualityResponse>

    @GET("/feed/@{stationId}/")
    suspend fun getAirQuality(
        @Path("stationId") stationId: Int
    ): ApiResult<AirQualityResponse>

    @GET("/search/")
    suspend fun getStations(
        @Query("keyword") query: String
    ): ApiResult<List<StationResponse>>
}