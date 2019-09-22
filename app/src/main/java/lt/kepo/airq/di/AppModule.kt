package lt.kepo.airq.di

import androidx.room.Room
import lt.kepo.airq.api.ApiClient
import lt.kepo.airq.api.ApiTokenInterceptor
import lt.kepo.airq.repository.AirQualityRepository
import lt.kepo.airq.repository.implementation.AirQualityRepositoryImpl
import lt.kepo.airq.db.AirQualityDatabase
import lt.kepo.airq.utility.AIR_Q_DATABASE_NAME
import lt.kepo.airq.utility.API_BASE_URL
import lt.kepo.airq.ui.viewmodel.AirQualityViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule : Module = module {

    single { createApiClientService<ApiClient>(okHttpClient = createHttpClient(), baseUrl = API_BASE_URL) }

    single { Room.databaseBuilder(get(), AirQualityDatabase::class.java, AIR_Q_DATABASE_NAME).build() }

    single { get<AirQualityDatabase>().airQualityDao() }

    factory<AirQualityRepository> { AirQualityRepositoryImpl( airQualityDao = get(), apiClient = get()) }

    viewModel { AirQualityViewModel(get(), get()) }
}

inline fun <reified T> createApiClientService(okHttpClient: OkHttpClient, baseUrl: String): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}

fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ApiTokenInterceptor())
        .build()
}
