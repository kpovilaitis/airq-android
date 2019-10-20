package lt.kepo.airq.data.db.dao

import androidx.room.*
import lt.kepo.airq.data.model.Station

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(station: Station): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(station: Station)

    @Delete
    suspend fun delete(station: Station)

    @Transaction
    suspend fun upsert(station: Station) {
        if (insert(station) == -1L) update(station)
    }

    @Query("SELECT * FROM stations")
    suspend fun getAll() : MutableList<Station>
}