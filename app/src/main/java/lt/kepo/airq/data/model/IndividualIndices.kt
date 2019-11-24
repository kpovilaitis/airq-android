package lt.kepo.airq.data.model

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IndividualIndices (
    @SerializedName("so2") @Embedded(prefix = "sulfur_oxide_") val sulfurOxide: PropertyDouble?,
    @SerializedName("o3") @Embedded(prefix = "ozone_") val ozone: PropertyDouble?,
    @SerializedName("pm10") @Embedded(prefix = "particle_10_") val particle10: PropertyInt?,
    @SerializedName("pm25") @Embedded(prefix = "particle_25_") val particle25: PropertyInt?
) : Parcelable
