package net.bi4vmr.tool.android.ability.privacy.appops

/**
 * AppOP实体。
 *
 * 对应AppOpsManager中的PackageOps类。
 *
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
     * OP编码。
     *
     * 对应[AppOps]枚举。
     */
    val opCode: Int,

    /**
     * OP名称，可以使用[AppOps.valueOf]方法根据[opCode]转换。
     */
    val opName: String,

    /**
     * 当前是否正在运行。
     *
     * 部分OP具有该属性，例如应用程序请求高精度GNSS位置信息时，将会返回 `MONITOR_HIGH_POWER_LOCATION(42)` 且该状态为"true"；
     * 当应用程序不再请求位置信息时，该状态为"false"。
     *
     * 部分OP没有该属性，例如应用程序请求精确位置或粗略位置信息时，该状态永远为"false"。
     */
    val isRunning: Boolean
)
