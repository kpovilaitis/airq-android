package lt.kepo.stations

interface GetStationsUseCase {

    suspend operator fun invoke(query: String): Result

    sealed class Result {

        data class Success(
            val stations: List<Station>
        ) : Result()

        object Error : Result()
    }
}