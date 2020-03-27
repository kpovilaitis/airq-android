package lt.kepo.network

import java.io.IOException

class ApiCallException(
    val statusCode: Int,
    message: String
) : IOException(
    message
)