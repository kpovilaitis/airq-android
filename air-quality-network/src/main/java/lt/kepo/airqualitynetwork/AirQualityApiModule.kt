package lt.kepo.airqualitynetwork

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.kepo.airqualitynetwork.response.ErrorResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AirQualityApiModule {

    @Provides
    @Singleton
    fun provideAirQualityApi(
        interceptor: ApiTokenInterceptor
    ): AirQualityApi =
        Retrofit.Builder()
            .baseUrl(
                AIR_QUALITY_API_URL
            ).addConverterFactory(
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
            ).client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        interceptor
                    )
                    .build()
            ).addCallAdapterFactory(
                AirQualityApiCallAdapter.Factory(
                    errorResponseAdapter = Moshi.Builder()
                        .build()
                        .adapter(ErrorResponse::class.java),
                )
            ).build()
            .create(
                AirQualityApi::class.java
            )
}
