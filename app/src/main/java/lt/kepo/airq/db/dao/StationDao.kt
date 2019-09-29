package lt.kepo.airq.db.dao

import androidx.room.*
import lt.kepo.airq.db.model.Station

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(station: Station): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(station: Station)

    @Delete
    fun delete(station: Station)

    @Transaction
    fun upsert(station: Station) {
        if (insert(station) == -1L) update(station)
    }

    @Query("SELECT * FROM stations")
    fun getAll() : List<Station>
}