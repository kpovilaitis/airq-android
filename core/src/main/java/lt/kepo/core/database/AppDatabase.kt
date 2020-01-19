package lt.kepo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import lt.kepo.core.database.dao.AirQualityDao
import lt.kepo.core.database.typeconverter.DateTypeConverter
import lt.kepo.core.model.AirQuality

@TypeConverters(DateTypeConverter::class)
@Database(
    entities = [ AirQuality::class ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airQualityDao(): AirQualityDao
}
