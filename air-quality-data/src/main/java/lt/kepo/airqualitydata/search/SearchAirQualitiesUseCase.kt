package lt.kepo.airqualitydata.search

import lt.kepo.airqualitydata.AirQualityListItem

interface SearchAirQualitiesUseCase {

    suspend operator fun invoke(
        query: String,
    ): Result

    sealed class Result {

        data class Success(
            val airQualities: List<AirQualityListItem>,
        ) : Result()

        object Error : Result()
    }
}