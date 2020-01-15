package lt.kepo.airq.data.api

import retrofit2.Response
import java.lang.Exception

data class ApiHttpResponse<T> (val status: ApiResponseStatus, val data: T, val message: String?)

sealed class ApiResponse<T> {
    companion object {
        fun <T> parse(error: Exception): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> parse(response: Response<ApiHttpResponse<T>>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()

                when {
                    response.code() == 204 -> ApiErrorResponse("Api returned empty response")

                    body == null -> ApiErrorResponse("unknown error")

                    body.status == ApiResponseStatus.OK -> ApiSuccessResponse(data = body.data)

                    else -> ApiErrorResponse(body.message ?: "unknown error")
                }
            } else {
                val msg = response.errorBody()?.string()

                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }

                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}

data class ApiErrorResponse<T>(val error: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val data: T) : ApiResponse<T>()

/**
 * @param transform transformation applied on success result
 *
 * @return - onSuccess - transformed result
 * - onError - same error
 */
inline fun <T> ApiResponse<T>.mapOnSuccess(transform: (T) -> T): ApiResponse<T> =
    when (this) {
        is ApiSuccessResponse -> ApiSuccessResponse(transform(this.data))
        is ApiErrorResponse -> this
    }

enum class ApiResponseStatus {
    OK,
    ERROR
}