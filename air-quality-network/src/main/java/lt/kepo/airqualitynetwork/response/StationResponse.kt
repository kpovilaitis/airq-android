package lt.kepo.airqualitynetwork.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StationResponse (
    @Json(name = "data") val data: Data,
) {
    @JsonClass(generateAdapter = true)
    data class Data (
        @Json(name = "uid") val id: Int,
        @Json(name = "aqi") val airQualityIndex: String,
        @Json(name = "station") val station: Station
    ) {
        @JsonClass(generateAdapter = true)
        data class Station (
            @Json(name = "name") var name: String
        )
    }
}
