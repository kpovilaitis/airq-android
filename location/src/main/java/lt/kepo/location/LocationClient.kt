package lt.kepo.location

interface LocationClient {

    suspend fun getLastLocation(): Location?
}