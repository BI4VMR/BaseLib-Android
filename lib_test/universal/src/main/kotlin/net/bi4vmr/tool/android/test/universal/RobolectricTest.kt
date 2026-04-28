package net.bi4vmr.tool.android.test.universal

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowLooper
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Robolectric基本测试类。
 *
 * 使用Robolectric提供默认的Application和Context，并集成MockK初始化逻辑与Kotlin协程测试工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
abstract class RobolectricTest {

    companion object {
        // 默认的 `runTest` 超时时间，单位：毫秒。
        private const val COROUTINE_TIMEOUT_DEFAULT = 60_000L
    }

    /**
     * Robolectric提供的模拟Application实例。
     *
     * 默认使用Spy模式，以便调用者按需改写方法或验证调用。
     */
    protected val mockApplication: Application =
        spyk(RuntimeEnvironment.getApplication(), recordPrivateCalls = true)

    /**
     * 应用级Context实例。
     */
    protected val mockContext: Context = spyk(mockApplication.applicationContext, recordPrivateCalls = true)

    @Before
    @CallSuper
    open fun setUp() {
        // 将Logcat输出重定向到Java控制台上
        ShadowLog.stream = System.out

        // Spy模式会生成新的实例，因此需要定义行为，确保Application的相关方法返回新的Context实例
        every { mockApplication.applicationContext } returns mockContext

        // 初始化MockK注解
        MockKAnnotations.init(this)
    }

    @After
    @CallSuper
    open fun tearDown() {
        // 撤销静态方法、构造方法等全局类Mock定义
        unmockkAll()
    }

    /**
     * 运行测试代码（支持Main调度器）。
     *
     * 基于TestScope的 `testScheduler` 创建Main调度器，便于测试包含此调度器的代码，可以使用 `advanceUntilIdle()` 等方法统一管理协程。
     *
     * @param[context] 需要额外添加的协程环境。默认为空。
     * @param[timeout] 最大允许的执行时长。默认为60秒。
     * @param[testBody] 测试代码。
     */
    protected fun runTestWithMain(
        context: CoroutineContext = EmptyCoroutineContext,
        timeout: Duration = COROUTINE_TIMEOUT_DEFAULT.seconds,
        testBody: suspend TestScope.() -> Unit
    ) {
        runTest(context, timeout) {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))
            testBody()
            Dispatchers.resetMain()
        }
    }

    /**
     * 推进并等待所有受TestScope管理的协程执行完毕。
     *
     * [advanceUntilIdle] 方法的封装，避免调用者手动添加 [ExperimentalCoroutinesApi] 注解。
     */
    protected fun TestScope.advanceDispatchers() {
        advanceUntilIdle()
    }

    /**
     * 推进并等待所有受TestScope管理的协程以及UI线程的Looper任务执行完毕。
     *
     * LiveData通过Looper分发更新，因此需要调用 [ShadowLooper.runUiThreadTasksIncludingDelayedTasks] ，而不能等待协程Main调度器。
     */
    protected fun TestScope.advanceDispatchersAndUILooper() {
        advanceUntilIdle()
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    }
}
