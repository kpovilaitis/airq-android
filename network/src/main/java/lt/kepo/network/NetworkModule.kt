package lt.kepo.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import lt.kepo.airqualityapi.AIR_QUALITY_API_URL
import lt.kepo.airqualityapi.AirQualityApi
import lt.kepo.airqualityapi.ApiTokenInterceptor
import lt.kepo.airqualityapi.LocalDateTimeAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideAirQualityApi(
        interceptor: ApiTokenInterceptor
    ): AirQualityApi =
        Retrofit.Builder()
            .baseUrl(
                AIR_QUALITY_API_URL
            )
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(
                            LocalDateTimeAdapter()
                        )
                        .add(
                            KotlinJsonAdapterFactory()
                        )
                        .build()
                )
            )
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        interceptor
                    )
                    .build()
            )
            .build()
            .create(
                AirQualityApi::class.java
            )
}
