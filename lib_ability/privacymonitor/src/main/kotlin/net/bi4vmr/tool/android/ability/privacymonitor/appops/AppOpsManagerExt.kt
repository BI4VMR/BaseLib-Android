package net.bi4vmr.tool.android.ability.privacymonitor.appops

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.util.Log
import net.bi4vmr.tool.android.ability.privacymonitor.common.LogConfig
import java.lang.reflect.Method
import java.util.concurrent.Executors

/**
 * AppOpsManager功能扩展。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class AppOpsManagerExt private constructor(mContext: Context) {

    companion object {
        // 本实例的生命周期跟随整个进程，不会导致内存泄露，因此可以忽略警告。
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AppOpsManagerExt? = null

        @JvmStatic
        fun getInstance(context: Context): AppOpsManagerExt {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = AppOpsManagerExt(context.applicationContext)
                    }
                }
            }
            return instance!!
        }

        private val TAG = "${LogConfig.TAG_PREFIX}${AppOpsManagerExt::class.java.simpleName}"
    }

    private val opsManagerClass = AppOpsManager::class.java
    private val opsManager: AppOpsManager = mContext.getSystemService(opsManagerClass)

    init {
        Log.i(TAG, "startWatchingActive")
        val exec = Executors.newSingleThreadExecutor()
        val arr = arrayOf(
            AppOpsManager.OPSTR_CAMERA,
            AppOpsManager.OPSTR_MONITOR_HIGH_POWER_LOCATION,
            AppOpsManager.OPSTR_RECORD_AUDIO
        )

        opsManager.startWatchingActive(arr, exec, object : AppOpsManager.OnOpActiveChangedListener {
            override fun onOpActiveChanged(op: String, uid: Int, packageName: String, active: Boolean) {
                Log.i("TestApp", "OnOpActiveChanged. Name:[$op] Active:[$active] PackageName:[$packageName] UID:[$uid]")
            }
        })
    }

    /**
     * 查询权限使用情况。
     *
     * 如果调用者没有被授予 `GET_APP_OPS_STATS` 权限，只能查到自身的结果。
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
                            // 将OP记录转换为PrivacyItem
                            val opName: String = AppOps.valueOf(opCode)?.toString() ?: "UNKNOWN"
                            result.add(OpEntity(packageName, uid, opCode, opName, modeCode, running).apply {
                                Log.i("TestApp", "${this}")
                            })
                        }
                    }
                }
            }

            return result
        } catch (e: Exception) {
            Log.e(TAG, "Reflect operate failed! Reason:[${e.message}]")
            e.printStackTrace()
            return result
        }
    }
}
