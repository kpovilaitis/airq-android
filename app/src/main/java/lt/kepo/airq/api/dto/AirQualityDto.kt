package lt.kepo.airq.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AirQualityDto (
    @SerializedName("idx") val stationId: Int,
    @SerializedName("aqi") val airQualityIndex: String,
    @SerializedName("dominentpol") val dominatingPollutant: String,
    @SerializedName("iaqi") val individualIndices: IndividualIndicesDto,
    @SerializedName("city") val city: CityDto,
    @SerializedName("time") val time: TimeDto,
    @Expose(deserialize = false, serialize = false) val isCurrentLocationQuality: Boolean
)