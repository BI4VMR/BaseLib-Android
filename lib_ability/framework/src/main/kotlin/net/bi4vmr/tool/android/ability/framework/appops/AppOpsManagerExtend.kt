package net.bi4vmr.tool.android.ability.framework.appops

import android.app.AppOpsManager
import android.content.Context
import net.bi4vmr.tool.android.ability.framework.common.ApplicationExtend
import net.bi4vmr.tool.android.ability.framework.util.FrameworkLog
import java.lang.reflect.Method
import java.util.concurrent.Executor

/**
 * AppOpsManager功能扩展。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
object AppOpsManagerExtend {

    private const val TAG: String = "BaseLib-AppOpsManagerExtend"

    private val appContext: Context = ApplicationExtend.getAppContext()
    private val opsManagerClass: Class<AppOpsManager> = AppOpsManager::class.java
    private val opsManager: AppOpsManager = appContext.getSystemService(opsManagerClass)

    /**
     * 获取OP名称对应的编码。
     *
     * @param[name] OP名称。
     * @return OP编码。如果无法识别，则返回 `-1` 。
     */
    fun opNameToCode(name: String): Int {
        try {
            val method: Method = opsManagerClass.getMethod("strOpToOp", String::class.java)
            return method.invoke(opsManager, name) as? Int ?: -1
        } catch (e: Exception) {
            FrameworkLog.logE(TAG, "Reflect operate failed! Reason:[${e.message}]", e)
            return -1
        }
    }

    /**
     * 获取OP编码对应的字符串名称。
     *
     * @param[code] OP编码。
     * @return 名称。如果数值超出范围则返回 `Unknown(<编号>)` 。
     */
    fun opCodeToName(code: Int): String {
        try {
            val method: Method = opsManagerClass.getMethod("opToPublicName", Int::class.java)
            return method.invoke(opsManager, code) as? String ?: "NULL"
        } catch (e: Exception) {
            FrameworkLog.logE(TAG, "Reflect operate failed! Reason:[${e.message}]", e)
            return "NULL"
        }
    }

    /**
     * 将编码数组转为名称数组。
     *
     * @param[codes] OP编码数组。
     * @return OP名称数组。
     */
    fun opCodeArrayToNameArray(codes: IntArray): Array<String> {
        val result: Array<String> = Array(codes.size) { "" }
        codes.forEachIndexed { index, code ->
            result[index] = opCodeToName(code)
        }

        return result
    }

    /**
     * 查询近期的OP事件。
     *
     * 如果调用者没有被授予 `GET_APP_OPS_STATS` 权限，只能查到自身产生的事件。
     *
     * @param[ops] OP类型数组。传入空值时表示不限制类型，将返回所有OP信息。
     * @return 列表，元素为[OpEntity]对象。
     */
    fun getPackagesOps(ops: IntArray? = null): List<OpEntity> {
        val result: MutableList<OpEntity> = mutableListOf()

        try {
            val method: Method = opsManagerClass.getMethod("getPackagesForOps", IntArray::class.java)
            // 获取应用程序的近期操作，返回值类型为List<AppOpsManager.PackageOps>，AppOpsManager.PackageOps是隐藏API。
            val packageOPList: List<*>? = method.invoke(opsManager, ops) as? List<*>
            packageOPList?.forEach { pkgOP ->
                if (pkgOP != null) {
                    // 获取包名
                    val methodGetPkgName = pkgOP.javaClass.getMethod("getPackageName")
                    val packageName: String = methodGetPkgName.invoke(pkgOP) as String
                    // 获取UID
                    val methodGetUID = pkgOP.javaClass.getMethod("getUid")
                    val uid: Int = methodGetUID.invoke(pkgOP) as Int
                    // 获取OP列表
                    val methodGetOps = pkgOP.javaClass.getMethod("getOps")
                    val opList: List<*> = methodGetOps.invoke(pkgOP) as List<*>
                    opList.forEach { entry ->
                        if (entry != null) {
                            // 获取OP代码
                            val methodGetOP = entry.javaClass.getMethod("getOp")
                            val opCode: Int = methodGetOP.invoke(entry) as Int
                            // 获取Mode代码
                            val methodGetMode = entry.javaClass.getMethod("getMode")
                            val modeCode: Int = methodGetMode.invoke(entry) as Int

                            // 获取当前是否正在运行
                            val methodIsRunning = entry.javaClass.getMethod("isRunning")
                            val running: Boolean = methodIsRunning.invoke(entry) as Boolean
                            // 将OP记录转换为OpEntity
                            val entity = OpEntity(packageName, uid, opCode, modeCode, running)
                            result.add(entity)

                            // 输出调试日志
                            FrameworkLog.logD(TAG, "GetPackagesOps. $entity")
                        }
                    }
                }
            }

            return result
        } catch (e: Exception) {
            FrameworkLog.logE(TAG, "Reflect operate failed! Reason:[${e.message}]", e)
            return result
        }
    }

    /**
     * 开始监听OP的运行状态变化。
     *
     * 监听各应用OP的 `isRunning` 状态变化事件。
     *
     * @param[ops] 感兴趣的OP名称数组。
     * @param[callback] 监听器具体实现。
     * @param[executor] 事件回调线程。若不特别指定，默认使用主线程执行回调。
     */
    @JvmOverloads
    fun startWatchingActive(
        ops: Array<String>,
        callback: AppOpsManager.OnOpActiveChangedListener,
        executor: Executor = appContext.mainExecutor,
    ) {
        FrameworkLog.logD(TAG, "StartWatchingActive. Ops:${ops.contentToString()}")
        opsManager.startWatchingActive(ops, executor, callback)
    }

    /**
     * 开始监听OP的运行状态变化。
     *
     * 监听各应用OP的 `isRunning` 状态变化事件。
     *
     * @param[ops] 感兴趣的OP编码数组。
     * @param[callback] 监听器具体实现。
     * @param[executor] 事件回调线程。若不特别指定，默认使用主线程执行回调。
     */
    @JvmOverloads
    fun startWatchingActive(
        ops: IntArray,
        callback: AppOpsManager.OnOpActiveChangedListener,
        executor: Executor = appContext.mainExecutor,
    ) {
        FrameworkLog.logD(TAG, "StartWatchingActive. Ops:${ops.contentToString()}")
        opsManager.startWatchingActive(opCodeArrayToNameArray(ops), executor, callback)
    }

    /**
     * 停止监听OP的运行状态变化。
     *
     * @param[callback] 监听器具体实现。
     */
    fun stopWatchingActive(callback: AppOpsManager.OnOpActiveChangedListener) {
        FrameworkLog.logD(TAG, "StopWatchingActive.")
        opsManager.stopWatchingActive(callback)
    }
}
