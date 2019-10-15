package lt.kepo.airq.data.api

import retrofit2.Response
import java.lang.Exception

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

                    else -> ApiErrorResponse(body.data?.toString() ?: "unknown error")
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

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val data: T) : ApiResponse<T>()

data class ApiHttpResponse<T> (val status: ApiResponseStatus, val data: T)