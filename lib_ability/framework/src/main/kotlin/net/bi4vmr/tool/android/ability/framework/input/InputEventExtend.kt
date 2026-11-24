package net.bi4vmr.tool.android.ability.framework.input

import android.view.InputEvent
import net.bi4vmr.tool.kotlin.common.reflect.call
import net.bi4vmr.tool.kotlin.common.reflect.requireMethod

/**
 * [InputEvent] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object InputEventExtend {

    private const val TAG: String = "InputEventExtend"


    /**
     * 设置屏幕 ID。
     *
     * 通常用于转发投屏画面的触摸事件到原始屏幕。
     *
     * @param[event] 交互事件。
     * @param[displayID] 显示屏 ID。
     */
    fun setDisplayID(event: InputEvent, displayID: Int) =
        event.requireMethod("setDisplayId", Int::class.javaPrimitiveType)
            .call(event, displayID)
}
