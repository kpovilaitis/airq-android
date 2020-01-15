package lt.kepo.airq.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import lt.kepo.airq.data.model.AirQuality
import lt.kepo.airq.utility.AIR_QUALITY_HERE_STATION_ID

@Dao
interface AirQualityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(airQuality: AirQuality): Long

    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    fun get(stationId: Int): LiveData<AirQuality>

    @Query("SELECT * FROM air_qualities ORDER BY is_current_location_quality DESC, city_name ASC")
    fun getAll(): LiveData<List<AirQuality>>

    @Query("SELECT station_id FROM air_qualities WHERE is_current_location_quality = 1")
    suspend fun getHereId(): Int

    @Query("DELETE FROM air_qualities WHERE is_current_location_quality = 1 AND station_id = $AIR_QUALITY_HERE_STATION_ID")
    suspend fun deleteHere()

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    suspend fun delete(stationId: Int)
}
