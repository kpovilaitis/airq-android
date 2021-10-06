package lt.kepo.airqualitynetwork

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
