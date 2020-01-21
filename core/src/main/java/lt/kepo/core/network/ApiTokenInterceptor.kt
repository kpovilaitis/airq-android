package lt.kepo.core.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiTokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("token", "4343dfb014a688af55503fbce70dc6b49f360cf3")
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}