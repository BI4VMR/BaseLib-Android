package net.bi4vmr.tool.android.ability.privacy.monitor

import android.app.AppOpsManager
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity

/**
 * OP表项筛选回调。
 *
 * [AppOpsMonitor]从[AppOpsManager]中获取到应用权限使用记录列表时，将会回调该接口中的方法，判断是否将表项加入结果列表。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
fun interface AppOpsFilterCallback {

    /**
     * 根据过滤规则确定是否将OP加入结果列表。
     *
     * @param[item] OP实体。
     * @return "true"表示采纳该OP，"false"表示忽略该OP。
     */
    fun test(item: OpEntity): Boolean
}
