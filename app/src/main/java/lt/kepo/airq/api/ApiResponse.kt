package lt.kepo.airq.api

data class ApiResponse<T> (
    val status: String,
    val data: T
)