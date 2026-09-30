package net.bi4vmr.tool.android.ability.privacymonitor

import net.bi4vmr.tool.android.ability.framework.appops.AppOpsManagerExtend
import net.bi4vmr.tool.android.ability.framework.appops.OpEntity

/**
 * OP表项筛选回调。
 *
 * [PrivacyMonitor]从[AppOpsManagerExtend]中获取到OP记录列表时，将会回调该接口中的方法，判断是否将表项加入结果列表。
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
