package lt.kepo.airq.data.model

import com.google.gson.annotations.SerializedName

data class Station (
    @SerializedName("uid") val id: Int,
    @SerializedName("aqi") val airQualityIndex: String,
    @SerializedName("station") val station: City
)