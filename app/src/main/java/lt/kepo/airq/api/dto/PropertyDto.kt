package lt.kepo.airq.api.dto

import com.google.gson.annotations.SerializedName

data class PropertyDto (
    @SerializedName("v") val value: Double
)