package lt.kepo.airq.di

import androidx.room.Room
import lt.kepo.airq.data.db.AppDatabase
import lt.kepo.airq.di.api.createApiClientService
import lt.kepo.airq.di.api.createHttpClient
import lt.kepo.airq.utility.AIR_Q_DATABASE_NAME
import lt.kepo.airq.utility.API_BASE_URL
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule : Module = module {

    single { createApiClientService(createHttpClient(), API_BASE_URL) }

    single { Room.databaseBuilder(get(), AppDatabase::class.java, AIR_Q_DATABASE_NAME).build() }

    single { get<AppDatabase>().airQualityDao() }

    single { get<AppDatabase>().stationDao() }
}
