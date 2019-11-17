package lt.kepo.airq.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import lt.kepo.airq.data.model.AirQuality

@Dao
interface AirQualityDao {
    @Query("SELECT * FROM air_qualities WHERE station_id = :stationId")
    fun get(stationId: Int): LiveData<AirQuality>

    @Query("SELECT * FROM air_qualities ORDER BY is_current_location_quality DESC, city_name ASC")
    suspend fun getAll(): List<AirQuality>

//    @Transaction
//    suspend fun upsertHere(airQuality: AirQuality) {
//        if (update(airQuality) == 0)
//            upsert(airQuality)
//    }

    @Query("DELETE FROM air_qualities WHERE is_current_location_quality = 1")
    suspend fun deleteHere()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(airQuality: AirQuality): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(airQuality: AirQuality): Int

    @Query("DELETE FROM air_qualities WHERE station_id = :stationId")
    suspend fun delete(stationId: Int)

    @Transaction
    suspend fun upsert(airQuality: AirQuality) {
        if (insert(airQuality) == -1L) update(airQuality)
    }
}