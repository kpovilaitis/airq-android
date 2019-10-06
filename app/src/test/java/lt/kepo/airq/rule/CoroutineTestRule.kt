package lt.kepo.airq.rule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.CoroutineContext

class CoroutineTestRule : TestRule, CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Unconfined

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            base.evaluate()
            this@CoroutineTestRule.cancel() // cancels CoroutineScope
        }
    }
}