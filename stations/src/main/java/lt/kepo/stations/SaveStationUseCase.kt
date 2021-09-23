package lt.kepo.stations

interface SaveStationUseCase {

    suspend operator fun invoke(stationId: Int): Result

    sealed class Result {

        object Success : Result()

        object Error : Result()
    }
}