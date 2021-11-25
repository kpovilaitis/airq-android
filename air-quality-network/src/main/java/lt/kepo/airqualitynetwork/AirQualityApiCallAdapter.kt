package lt.kepo.airqualitynetwork

import com.squareup.moshi.JsonAdapter
import lt.kepo.airqualitynetwork.response.ErrorResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class AirQualityApiCallAdapter<Result>(
    private val errorResponseAdapter: JsonAdapter<ErrorResponse>,
    private val successType: Type,
) : CallAdapter<Result, Call<ApiResult<Result>>> {

    override fun responseType(): Type = successType

    override fun adapt(
        call: Call<Result>,
    ) = AirQualityApiCall(
        delegate = call,
        errorResponseAdapter = errorResponseAdapter,
    )

    class Factory(
        private val errorResponseAdapter: JsonAdapter<ErrorResponse>,
    ) : CallAdapter.Factory() {

        override fun get(
            returnType: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? = try {
            val enclosingType = (returnType as ParameterizedType)
            val responseType = getParameterUpperBound(0, enclosingType)

            if (
                enclosingType.rawType != Call::class.java ||
                    responseType !is ParameterizedType ||
                    responseType.rawType != ApiResult::class.java
            ) {
                null
            } else {
                AirQualityApiCallAdapter<Any>(
                    errorResponseAdapter = errorResponseAdapter,
                    successType = getParameterUpperBound(0, responseType)
                )
            }
        } catch (ex: ClassCastException) {
            null
        }
    }
}
