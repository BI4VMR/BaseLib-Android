package net.bi4vmr.tool.android.ability.framework.appops

/**
 * 实体类：OpEntity。
 *
 * 对应 AppOpsManager 中的 PackageOps 类。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
data class OpEntity(

    /**
     * 包名。
     */
    val packageName: String,

    /**
     * UID。
     */
    val uid: Int,

    /**
     * OP 编码。
     *
     * 对应 [AppOps] 枚举。
     */
    val opCode: Int,

    /**
     * 请求结果编码。
     */
    val modeCode: Int,

    /**
     * 当前 OP 是否正在运行。
     *
     * 仅有部分 OP 具有该属性，例如：当程序请求高精度 GNSS 信息时，OP `MONITOR_HIGH_POWER_LOCATION(42)` 的运行状态将变为 `true`，
     * 当程序停止该操作后，OP的运行状态将变为 `false`。另外一部分 OP 没有该属性，它们是瞬态的，运行状态永远为 `false`，例如：粗略位置
     * `COARSE_LOCATION(0)` 和精确位置 `FINE_LOCATION(1)`。
     */
    val running: Boolean
)
