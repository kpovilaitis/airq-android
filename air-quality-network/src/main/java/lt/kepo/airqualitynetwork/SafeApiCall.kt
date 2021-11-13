package lt.kepo.airqualitynetwork

import lt.kepo.airqualitynetwork.response.AirQualityApiResponse
import retrofit2.Response

@Suppress("TooGenericExceptionCaught")
suspend fun <Result> AirQualityApi.call(
    call: suspend AirQualityApi.() -> Response<AirQualityApiResponse<Result>>
): ApiResult<Result> =
    try {
        call(this).toApiResult()
    } catch (cause: Exception) {
        ApiResult.Error(
            message = cause.message
        )
    }

@Suppress("TooGenericExceptionCaught")
private fun <Result> Response<AirQualityApiResponse<Result>>.toApiResult(): ApiResult<Result> =
    if (isSuccessful) {
        ApiResult.Success(
            code = code(),
            data = body()?.data ?: error("Success response body can not be null")
        )
    } else {
        ApiResult.Error(
            code = code(),
            message = errorBody()?.string() ?: message() ?: "Unknown api error"
        )
    }
