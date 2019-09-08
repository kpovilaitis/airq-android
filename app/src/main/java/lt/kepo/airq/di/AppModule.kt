package lt.kepo.airq.di

import androidx.room.Room
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import lt.kepo.airq.api.ApiClient
import lt.kepo.airq.api.ApiTokenInterceptor
import lt.kepo.airq.data.models.Property
import lt.kepo.airq.db.airquality.AirQualityRepository
import lt.kepo.airq.db.airquality.AirQualityRepositoryImpl
import lt.kepo.airq.db.AirQualityDatabase
import lt.kepo.airq.utilities.AIRQ_DATABASE_NAME
import lt.kepo.airq.utilities.API_BASE_URL
import lt.kepo.airq.viewmodels.AirQualityViewModel
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule : Module = module {

    single { createApiClientService<ApiClient>(
        okHttpClient = createHttpClient(),
        factory = RxJava2CallAdapterFactory.create(),
        baseUrl = API_BASE_URL
    ) }

    single { Room.databaseBuilder(get(), AirQualityDatabase::class.java, AIRQ_DATABASE_NAME).build() }

    single { get<AirQualityDatabase>().airQualityDao() }

    factory<AirQualityRepository> {
        AirQualityRepositoryImpl(
            airQualityDao = get(),
            apiClient = get()
        )
    }

    viewModel { AirQualityViewModel(get()) }
}

inline fun <reified T> createApiClientService(okHttpClient: OkHttpClient, factory: CallAdapter.Factory, baseUrl: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(factory)
        .client(okHttpClient)
        .build()

    return retrofit.create(T::class.java)
}

fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ApiTokenInterceptor())
        .build()
}
