package lt.kepo.airqualitynetwork

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiTokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("token", AIR_QUALITY_API_AUTH_TOKEN)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}