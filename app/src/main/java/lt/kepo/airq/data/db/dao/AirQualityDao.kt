package lt.kepo.airq.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import lt.kepo.airq.data.model.AirQuality
import java.util.*

@Dao
abstract class AirQualityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(airQuality: AirQuality): Long

    suspend fun insertWithTimeStamp(airQuality: AirQuality) = insert(airQuality.apply { updatedAt = Date() })

    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    abstract fun get(stationId: Int): LiveData<AirQuality>

    @Query("SELECT * FROM air_qualities WHERE is_current_location_quality = 0 ORDER BY city_name ASC")
    abstract fun getAll(): LiveData<List<AirQuality>>

    @Query("SELECT * FROM air_qualities WHERE is_current_location_quality = 1")
    abstract fun getHere(): LiveData<AirQuality>

    @Query("SELECT station_id FROM air_qualities WHERE is_current_location_quality = 1")
    abstract suspend fun getHereId(): Int

    @Query("DELETE FROM air_qualities WHERE is_current_location_quality = 1")
    abstract suspend fun deleteHere()

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    abstract suspend fun delete(stationId: Int)
}
