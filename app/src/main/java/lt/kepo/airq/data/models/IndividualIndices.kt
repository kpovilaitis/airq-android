package lt.kepo.airq.data.models

import com.google.gson.annotations.SerializedName

data class IndividualIndices(
    @SerializedName("so2") val sulfurOxide: Property,
    @SerializedName("o3") val ozone: Property,
    @SerializedName("pm10") val particle10: Property,
    @SerializedName("pm25") val particle25: Property
)