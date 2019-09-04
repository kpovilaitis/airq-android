package lt.kepo.airq.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "air_qualities")
data class AirQuality (
    @SerializedName("aqi") @PrimaryKey @ColumnInfo(name = "id")  val airQualityIndex: Int,
    @SerializedName("idx") val stationId: Int,
    @SerializedName("dominentpol") val dominatingPollutant: String,
    @SerializedName("iaqi") val individualIndices: IndividualIndices,
    @SerializedName("city") val city: City
)