package lt.kepo.airq.data.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class IndividualIndices (
    @SerializedName("so2") @Embedded(prefix = "sulfur_oxide_") val sulfurOxide: Property,
    @SerializedName("o3") @Embedded(prefix = "ozone_") val ozone: Property,
    @SerializedName("pm10") @Embedded(prefix = "particle_10_") val particle10: Property,
    @SerializedName("pm25") @Embedded(prefix = "particle_25_") val particle25: Property
)