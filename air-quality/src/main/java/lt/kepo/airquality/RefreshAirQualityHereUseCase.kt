package lt.kepo.airquality

interface RefreshAirQualityHereUseCase {

    suspend operator fun invoke(): Result

    sealed class Result {

        object Success: Result()

        object Error: Result()
    }
}
