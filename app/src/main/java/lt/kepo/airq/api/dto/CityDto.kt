package lt.kepo.airq.api.dto

import com.google.gson.annotations.SerializedName

data class CityDto (
    @SerializedName("name") val name: String
)