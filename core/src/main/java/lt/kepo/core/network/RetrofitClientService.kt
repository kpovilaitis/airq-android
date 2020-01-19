package lt.kepo.core.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

fun createApiClientService(okHttpClient: OkHttpClient, baseUrl: String): HttpClient {
    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .registerTypeAdapterFactory(LowercaseEnumTypeAdapterFactory())
        .create()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(HttpClient::class.java)
}

fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(ApiTokenInterceptor())
        .build()
}