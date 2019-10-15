package lt.kepo.airq.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stations")
data class Station (
    @SerializedName("uid") @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @SerializedName("aqi") @ColumnInfo(name = "air_quality_index") var airQualityIndex: String,
    @SerializedName("station") @Embedded(prefix = "city_") var station: City
)