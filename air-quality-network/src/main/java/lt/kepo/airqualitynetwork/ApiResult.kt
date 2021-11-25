package lt.kepo.airqualitynetwork

sealed class ApiResult<out T> {

    data class Success<T>(
        val data: T,
        val code: Int = 200
    ) : ApiResult<T>()

    sealed class Error : ApiResult<Nothing>() {

        object Network : Error()

        data class Server(
            val code: Int? = null,
            val message: String? = null
        ) : Error()
    }
}
