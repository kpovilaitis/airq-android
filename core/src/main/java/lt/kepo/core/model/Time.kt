package lt.kepo.core.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Time (
    @SerializedName("s") @ColumnInfo(name = "local_recorded") var localTimeRecorded: Date?
) : Parcelable {
    override fun toString(): String {
        return if (localTimeRecorded == null) {
            "-"
        } else {
            SimpleDateFormat("d'd' H'h'", Locale.getDefault()).format(localTimeRecorded!!)
        }
    }
}