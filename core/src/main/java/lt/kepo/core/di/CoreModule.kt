package lt.kepo.core.di

import androidx.room.Room
import lt.kepo.core.database.AppDatabase
import lt.kepo.core.network.createApiClientService
import lt.kepo.core.network.createHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {

    single { createApiClientService(createHttpClient(), "https://api.waqi.info") }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "air_quality_database"
        )
            .build()
    }
}
