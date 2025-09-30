package net.bi4vmr.tool.android.common.logcat.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.bi4vmr.tool.android.common.logcat.Level
import net.bi4vmr.tool.android.common.logcat.LogUtil
import java.util.concurrent.ConcurrentHashMap

/**
 * 周期性日志输出工具。
 *
 * @author bi4vmr@outlook.com
 */
object CycleLogger {

    // 工具调试专用标签
    private val TAG: String = "BaseLib-${CycleLogger::class.java.simpleName}"

    // 任务集合
    private val contents: MutableMap<String, Job> = ConcurrentHashMap<String, Job>()

    /**
     * 获取任务列表。
     *
     * @return 任务名称集合。
     */
    @JvmStatic
    fun getTasks(): Set<String> {
        return contents.keys
    }

    /**
     * 新增任务。
     *
     * @param[name] 任务名称。
     * @param[interval] 输出间隔，单位为毫秒。
     * @param[level] 日志级别。
     * @param[tag] 日志Tag。
     * @param[content] 日志内容。
     */
    @JvmStatic
    fun addTask(name: String, interval: Long, level: Level, tag: String?, content: String) =
        addTask(name, interval, level, tag) { content }

    /**
     * 新增任务。
     *
     * 本方法可以通过接口回调动态获取日志内容并输出。
     *
     * @param[name] 任务名称。
     * @param[interval] 输出间隔，单位为毫秒。
     * @param[level] 日志级别。
     * @param[tag] 日志Tag。
     * @param[task] 日志内容回调方法。
     */
    @JvmStatic
    fun addTask(name: String, interval: Long, level: Level, tag: String?, task: suspend () -> String) {
        // 校验该任务是否已存在
        if (contents.contains(name)) {
            throw IllegalStateException("Task already exists!")
        }

        val job: Job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val log: String = task()
                LogUtil.print(level, log, tag)
                delay(interval)
            }
        }

        contents[name] = job
    }

    /**
     * 取消任务。
     *
     * @param[name] 任务名称。
     */
    @JvmStatic
    fun removeTask(name: String) {
        val job: Job? = contents[name]
        job?.cancel()

        contents.remove(name)
    }

    /**
     * 取消所有任务。
     */
    @JvmStatic
    fun clearTask() {
        contents.values.forEach {
            it.cancel()
        }

        contents.clear()
    }
}
