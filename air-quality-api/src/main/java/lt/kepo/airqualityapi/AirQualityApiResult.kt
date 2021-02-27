package lt.kepo.airqualityapi

sealed class ApiResult<out T> {

    data class Success<T>(
        val data: T,
        val code: Int = 200
    ) : ApiResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String? = null
    ) : ApiResult<Nothing>()

    fun toUnitResult(): ApiResult<Unit> = when (this) {
        is Success -> Success(Unit, code)
        is Error -> this
    }
}

/**
 * @param transform transformation applied on success result
 *
 * @return - onSuccess - transformed result
 * - onError - same error
 */
inline fun <T, R> ApiResult<T>.mapOnSuccess(transform: (T) -> ApiResult<R>): ApiResult<R> =
    when (this) {
        is ApiResult.Success -> transform(this.data)
        is ApiResult.Error -> this
    }