package lt.kepo.airqualityapi.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AirQualityResponse (
    @Json(name = "idx") val stationId: Int,
    @Json(name = "aqi") val airQualityIndex: String,
    @Json(name = "iaqi") val individualIndices: IndividualIndicesResponse,
    @Json(name = "city") val city: CityResponse,
    @Json(name = "time") val time: TimeResponse
) {

    @JsonClass(generateAdapter = true)
    data class IndividualIndicesResponse (
        @Json(name = "so2") val sulfurOxide: ValueResponse?,
        @Json(name = "o3") val ozone: ValueResponse?,
        @Json(name = "pm10") val particle10: ValueResponse?,
        @Json(name = "pm25") val particle25: ValueResponse?
    ) {

        @JsonClass(generateAdapter = true)
        data class ValueResponse (
            @Json(name = "v") val value: Double
        )
    }

    @JsonClass(generateAdapter = true)
    data class CityResponse (
        @Json(name = "name") val name: String
    )

    @JsonClass(generateAdapter = true)
    data class TimeResponse (
        @Json(name = "s") val localTimeRecorded: Date
    )
}
