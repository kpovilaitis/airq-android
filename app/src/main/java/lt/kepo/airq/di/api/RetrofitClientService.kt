package lt.kepo.airq.di.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> createApiClientService(okHttpClient: OkHttpClient, baseUrl: String): T {
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(LowercaseEnumTypeAdapterFactory())
        .create()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}

fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ApiTokenInterceptor())
        .build()
}