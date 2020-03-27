package lt.kepo.airqualitydatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirQualityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        airQuality: AirQualityEntity
    ): Long

    @Query(
        """
        SELECT * FROM air_qualities 
        WHERE station_id = :stationId
        """
    )
    fun get(
        stationId: Int
    ): Flow<AirQualityEntity>

    @Query(
        """
        SELECT * FROM air_qualities 
        """
    )
    fun getAll(): Flow<List<AirQualityEntity>>

    @Query(
        """
        DELETE FROM air_qualities 
        WHERE station_id = :stationId
        """
    )
    suspend fun delete(
        stationId: Int
    )
}
