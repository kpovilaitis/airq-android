package lt.kepo.airq.api.dto

import com.google.gson.annotations.SerializedName

data class StationDto (
    @SerializedName("uid") val id: Int,
    @SerializedName("aqi") val airQualityIndex: String,
    @SerializedName("station") var station: CityDto
)