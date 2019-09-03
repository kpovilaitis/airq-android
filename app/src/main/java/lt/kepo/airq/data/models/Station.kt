package lt.kepo.airq.data.models

import com.google.gson.annotations.SerializedName

data class Station (
    @SerializedName("uid") val id: Int,
    @SerializedName("aqi") val airQualityIndex: Int,
    @SerializedName("station") var station: City
)