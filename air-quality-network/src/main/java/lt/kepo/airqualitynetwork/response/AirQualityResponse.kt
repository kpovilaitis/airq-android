package lt.kepo.airqualitynetwork.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AirQualityResponse (
    @Json(name = "data") val dataValue: Data,
) {
    @JsonClass(generateAdapter = true)
    data class Data (
        @Json(name = "idx") val stationId: Int,
        @Json(name = "aqi") val airQualityIndex: String,
        @Json(name = "iaqi") val individualIndices: IndividualIndices,
        @Json(name = "city") val city: City,
        @Json(name = "time") val time: Time
    ) {
        @JsonClass(generateAdapter = true)
        data class IndividualIndices (
            @Json(name = "so2") val sulfurOxide: Value?,
            @Json(name = "o3") val ozone: Value?,
            @Json(name = "pm10") val particle10: Value?,
            @Json(name = "pm25") val particle25: Value?
        ) {

            @JsonClass(generateAdapter = true)
            data class Value (
                @Json(name = "v") val value: Double
            )
        }

        @JsonClass(generateAdapter = true)
        data class City (
            @Json(name = "name") val name: String
        )

        @JsonClass(generateAdapter = true)
        data class Time (
            @Json(name = "s") val localTimeRecorded: Date
        )
    }
}
