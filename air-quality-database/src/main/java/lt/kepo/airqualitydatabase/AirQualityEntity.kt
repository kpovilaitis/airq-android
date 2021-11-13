package lt.kepo.airqualitydatabase

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "air_qualities")
data class AirQualityEntity (
    @PrimaryKey
    @ColumnInfo(name = "station_id")
    val stationId: Int,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "air_quality_index")
    val airQualityIndex: String,

    @ColumnInfo(name = "sulfur_oxide")
    val sulfurOxide: Double?,

    @ColumnInfo(name = "ozone")
    val ozone: Double?,

    @ColumnInfo(name = "particle_10")
    val particle10: Double?,

    @ColumnInfo(name = "particle_25")
    val particle25: Double?,

    @ColumnInfo(name = "is_current_location")
    val isCurrentLocationQuality: Boolean,

    @ColumnInfo(name = "local_time_recorded")
    val localTimeRecorded: Date,
)
