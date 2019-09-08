package lt.kepo.airq.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class IndividualIndices(
    @SerializedName("so2") @Embedded(prefix = "so2_") @Ignore val sulfurOxide: Property,
    @SerializedName("o3") @Embedded(prefix = "o3_") @Ignore val ozone: Property,
    @SerializedName("pm10") @Embedded(prefix = "pm10_") @Ignore val particle10: Property,
    @SerializedName("pm25") @Embedded(prefix = "pm25_") @Ignore val particle25: Property
)