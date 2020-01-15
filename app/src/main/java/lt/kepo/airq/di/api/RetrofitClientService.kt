package lt.kepo.airq.di.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import lt.kepo.airq.data.api.HttpClient
import lt.kepo.airq.utility.AIR_Q_DATE_FORMAT
import okhttp3.OkHttpClient

fun createApiClientService(okHttpClient: OkHttpClient, baseUrl: String): HttpClient {
    val gson = GsonBuilder()
        .setDateFormat(AIR_Q_DATE_FORMAT)
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