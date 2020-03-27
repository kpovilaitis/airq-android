package lt.kepo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.airqualitydatabase.AirQualityEntity
import lt.kepo.database.typeconverter.DateTypeConverter

@TypeConverters(DateTypeConverter::class)
@Database(
    entities = [
        AirQualityEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun airQualityDao(): AirQualityDao
}
