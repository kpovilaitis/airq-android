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
    fun getLive(stationId: Int): LiveData<AirQuality>

    @Query("SELECT * FROM air_qualities ORDER BY station_id == $AIR_QUALITY_HERE_STATION_ID DESC, city_name ASC")
    fun getAllLive(): LiveData<List<AirQuality>>

    @Query("SELECT * FROM air_qualities ORDER BY station_id == $AIR_QUALITY_HERE_STATION_ID DESC, city_name ASC")
    suspend fun getAll(): List<AirQuality>

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    suspend fun delete(stationId: Int)
}
