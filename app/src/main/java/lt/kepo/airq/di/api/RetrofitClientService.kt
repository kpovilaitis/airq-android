package lt.kepo.airq.di.api

import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.Route
import okhttp3.OkHttpClient
import java.io.IOException


inline fun <reified T> createApiClientService(okHttpClient: OkHttpClient, baseUrl: String): T {
    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
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