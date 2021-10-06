package lt.kepo.airqualitydata.refresh

interface RefreshAirQualityHereUseCase {

    suspend operator fun invoke(): Result

    sealed class Result {

        object Success: Result()

        object Error: Result()
    }
}
