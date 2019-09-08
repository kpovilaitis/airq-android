package lt.kepo.airq.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "air_qualities")
data class AirQuality (
    @SerializedName("idx") @PrimaryKey @ColumnInfo(name = "station_id") val stationId: Int,
    @SerializedName("aqi") @ColumnInfo(name = "air_quality_index") val airQualityIndex: Int,
    @SerializedName("dominentpol") @ColumnInfo(name = "dominating_pollutant") val dominatingPollutant: String,
    @SerializedName("iaqi") @Embedded val individualIndices: IndividualIndices,
    @SerializedName("city") @Embedded val city: City
)