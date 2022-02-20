package lt.kepo.core

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CollectAsStateWithLifecycle {
}

@Composable
fun <T : R, R> Flow<T>.collectAsStateWithLifecycle(
    initial: R,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<R> {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    return remember(this, lifecycle) {
        flowWithLifecycle(lifecycle, minActiveState)
    }.collectAsState(initial)
}