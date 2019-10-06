package lt.kepo.airq.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import lt.kepo.airq.api.ApiSuccessResponse
import lt.kepo.airq.db.model.Station
import lt.kepo.airq.repository.stations.StationsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject


class UpdateLocalStationsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    private val repository by inject<StationsRepository>()

    override suspend fun doWork(): Result = coroutineScope {
        val stations = withContext(Dispatchers.IO) { repository.getLocalAllStations() }

        val jobs = stations.map {
            async {
                when (val response = withContext(Dispatchers.IO) { repository.getStation(it.id) }) {
                    is ApiSuccessResponse -> {
                        val responseStation = Station.build(response.data)

                        stations.find{ it.id == responseStation.id }?.airQualityIndex = responseStation.airQualityIndex

                        launch (Dispatchers.IO) { repository.insertStation(it) }
                    }
                    else -> Result.failure()
                }
            }
        }

        jobs.awaitAll()

        Result.success()
    }
}