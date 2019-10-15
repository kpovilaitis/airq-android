package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import java.util.*

data class Time (
    @SerializedName("s") @ColumnInfo(name = "local") val localTime: Date
)