package net.bi4vmr.tool.android.ability.privacy.monitor

import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity

/**
 * OP列表监听器。
 *
 * @author bi4vmr@outlook.com
 */
fun interface AppOpsListener {

    /**
     * OP列表改变事件。
     *
     * @param[ops] OpEntity列表。
     */
    fun onChange(ops: List<OpEntity>)
}
