package lt.kepo.airqualityapi.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationResponse (
    @Json(name = "uid") val id: Int,
    @Json(name = "aqi") val airQualityIndex: String,
    @Json(name = "station") val station: StationResponse
) {

    @JsonClass(generateAdapter = true)
    data class StationResponse (
        @Json(name = "name") var name: String
    )
}
