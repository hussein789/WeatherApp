import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseTestClass {
    @get:Rule
    var mainCoroutineScopeRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutionRule = InstantTaskExecutorRule()
}