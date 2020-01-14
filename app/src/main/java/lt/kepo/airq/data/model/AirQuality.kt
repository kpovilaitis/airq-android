package lt.kepo.airq.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import lt.kepo.airq.utility.AIR_QUALITY_UPDATE_THRESHOLD
import java.util.*

@Parcelize
@Entity(tableName = "air_qualities")
data class AirQuality (
    @SerializedName("idx") @PrimaryKey @ColumnInfo(name = "station_id") val stationId: Int,
    @SerializedName("aqi") @ColumnInfo(name = "air_quality_index") var airQualityIndex: String,
    @SerializedName("dominentpol") @ColumnInfo(name = "dominating_pollutant") var dominatingPollutant: String,
    @SerializedName("iaqi") @Embedded var individualIndices: IndividualIndices,
    @SerializedName("city") @Embedded(prefix = "city_") val city: City,
    @SerializedName("time") @Embedded(prefix = "time_") val time: Time,
    @ColumnInfo(name = "updated_at") var updatedAt: Date,
    @Expose(deserialize = false, serialize = false) @ColumnInfo(name = "is_current_location_quality") var isCurrentLocationQuality: Boolean
) : Parcelable {
    fun shouldUpdate() = updatedAt.before(Date(Date().time - AIR_QUALITY_UPDATE_THRESHOLD))
}