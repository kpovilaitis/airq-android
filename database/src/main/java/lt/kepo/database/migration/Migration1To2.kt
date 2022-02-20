package lt.kepo.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS air_qualities_tmp (
                    station_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    address TEXT NOT NULL,
                    air_quality_index TEXT NOT NULL,
                    sulfur_oxide REAL,
                    ozone REAL,
                    particle_10 REAL,
                    particle_25 REAL,
                    is_current_location INTEGER NOT NULL,
                    local_time_recorded INTEGER NOT NULL
                )
                """,
            )
            execSQL(
                """
                 INSERT INTO air_qualities_tmp
                 SELECT station_id,
                    city_name,
                    air_quality_index,
                    sulfur_oxide_value,
                    ozone_value,
                    particle_10_value,
                    particle_25_value,
                    station_id = -1,
                    time_local_recorded
                 FROM air_qualities
                 """,
            )
            execSQL("DROP TABLE air_qualities")
            execSQL("ALTER TABLE air_qualities_tmp RENAME TO air_qualities")
        }
    }
}
