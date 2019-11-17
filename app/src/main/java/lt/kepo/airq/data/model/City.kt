package lt.kepo.airq.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City (
    @SerializedName("name") var name: String
) : Parcelable