package net.bi4vmr.tool.android.ability.privacy

import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity

/**
 * OP列表监听器。
 *
 * @author bi4vmr@outlook.com
 */
interface AppOpsListener {

    /**
     * OP列表改变事件。
     *
     * @param[ops] OpEntity列表。
     */
    fun onChange(ops: List<OpEntity>)
}
