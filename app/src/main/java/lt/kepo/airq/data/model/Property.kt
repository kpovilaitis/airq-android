package lt.kepo.airq.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Property (
    @SerializedName("v") val value: Double
) : Parcelable