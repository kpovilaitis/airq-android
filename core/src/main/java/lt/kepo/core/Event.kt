package lt.kepo.core

open class Event<out T>(
    private val content: T,
) {

    private var hasBeenHandled = false

    val notHandledContent: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
}

class SimpleEvent : Event<Unit>(Unit)