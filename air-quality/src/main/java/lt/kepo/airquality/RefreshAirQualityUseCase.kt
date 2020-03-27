package lt.kepo.airquality

interface RefreshAirQualityUseCase {

    suspend operator fun invoke(
        stationId: Int
    ): Result

    sealed class Result {

        object Success: Result()

        object Error: Result()
    }
}
