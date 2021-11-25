package lt.kepo.airqualitynetwork

import com.squareup.moshi.JsonAdapter
import lt.kepo.airqualitynetwork.response.ErrorResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class AirQualityApiCall<R>(
    private val delegate: Call<R>,
    private val errorResponseAdapter: JsonAdapter<ErrorResponse>,
) : Call<ApiResult<R>> {

    override fun enqueue(callback: Callback<ApiResult<R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                callback.onResponse(
                    this@AirQualityApiCall,
                    Response.success(
                        if (response.isSuccessful) {
                            ApiResult.Success(
                                code = response.code(),
                                data = response.body() ?: error("Success response body can not be null")
                            )
                        } else {
                            val error = response.errorBody()
                                ?.let { errorResponseAdapter.fromJson(it.string()) }
                                ?.data

                            ApiResult.Error.Server(
                                code = response.code(),
                                message = error?.message,
                            )
                        }
                    )
                )
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) =
                callback.onResponse(
                    this@AirQualityApiCall,
                    Response.success(
                        ApiResult.Error.Network
                    )
                )
        }
    )

    override fun clone(): Call<ApiResult<R>> =
        AirQualityApiCall(delegate, errorResponseAdapter)

    override fun execute(): Response<ApiResult<R>> =
        throw UnsupportedOperationException("AirQualityApiCall doesn't support execute")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
