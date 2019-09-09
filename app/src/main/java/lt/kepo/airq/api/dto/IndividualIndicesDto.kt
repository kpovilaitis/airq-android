package lt.kepo.airq.api.dto

import com.google.gson.annotations.SerializedName

data class IndividualIndicesDto(
    @SerializedName("so2") val sulfurOxide: PropertyDto?,
    @SerializedName("o3") val ozone: PropertyDto?,
    @SerializedName("pm10") val particle10: PropertyDto?,
    @SerializedName("pm25") val particle25: PropertyDto?
)