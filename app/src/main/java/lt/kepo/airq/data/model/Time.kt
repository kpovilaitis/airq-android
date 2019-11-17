package lt.kepo.airq.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import lt.kepo.airq.utility.AIR_Q_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Time (
    @SerializedName("s") @ColumnInfo(name = "local_recorded") var localTimeRecorded: Date
) : Parcelable {
    override fun toString(): String {
        return SimpleDateFormat(AIR_Q_DATE_FORMAT, Locale.getDefault()).format(localTimeRecorded)
    }
}