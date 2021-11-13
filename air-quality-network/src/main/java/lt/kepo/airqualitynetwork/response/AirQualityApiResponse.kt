package lt.kepo.airqualitynetwork.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AirQualityApiResponse<T> (
    @Json(name = "data") val data: T
)
