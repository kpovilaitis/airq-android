package lt.kepo.airq.di.api

import lt.kepo.airq.utility.API_AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class ApiTokenInterceptor: Interceptor {

    /**
     * Interceptor class for adding auth tokens to every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("token", API_AUTH_TOKEN)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}