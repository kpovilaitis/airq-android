package lt.kepo.airqualitynetwork.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Response
import java.io.IOException

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "data") val data: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "message") val message: String?,
    )
}
