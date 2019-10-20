package lt.kepo.airq.data.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import lt.kepo.airq.utility.AIR_Q_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

data class Time (
    @SerializedName("s") @ColumnInfo(name = "local_recorded") var localTimeRecorded: Date
) {
    override fun toString(): String {
        return SimpleDateFormat(AIR_Q_DATE_FORMAT, Locale.getDefault()).format(localTimeRecorded)
    }
}