package lt.kepo.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lt.kepo.airqualitydatabase.AirQualityDao
import lt.kepo.database.migration.MIGRATION_1_2
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "air_quality_database"
        ).addMigrations(
            MIGRATION_1_2,
        ).build()

    @Provides
    @Singleton
    fun provideAirQualityDao(
        appDatabase: AppDatabase
    ): AirQualityDao =
        appDatabase.airQualityDao()
}
