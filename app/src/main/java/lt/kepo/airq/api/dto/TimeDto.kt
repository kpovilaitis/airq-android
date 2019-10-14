package lt.kepo.airq.api.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class TimeDto (
    @SerializedName("s") val localTime: Date
)