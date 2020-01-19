package lt.kepo.core.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertyInt (
    @SerializedName("v") val value: Int
) : Parcelable