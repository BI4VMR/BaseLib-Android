package net.bi4vmr.tool.android.ability.framework.input

import android.content.Context
import android.hardware.input.InputManager
import android.view.InputEvent
import net.bi4vmr.tool.android.ability.framework.common.ApplicationExtend
import net.bi4vmr.tool.kotlin.common.reflect.callForSafe
import net.bi4vmr.tool.kotlin.common.reflect.requireMethod

/**
 * [InputManager] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object InputManagerExtend {

    private const val TAG: String = "InputManagerExtend"

    private val appContext: Context = ApplicationExtend.getAppContext()

    private val inputManager: InputManager = appContext.getSystemService(InputManager::class.java)


    /**
     * 注入交互事件。
     *
     * 调用者应当具有 `Manifest.permission.INJECT_EVENTS` 权限。
     *
     * 方法原型详见 [Android Code Search](https://cs.android.com/android/platform/superproject/+/android-latest-release:frameworks/base/core/java/android/hardware/input/InputManager.java?hl=zh-cn)。
     *
     * @param[event] 交互事件。
     * @param[mode] 注入模式。默认为 [InjectMode.ASYNC]。
     * @return `true` 表示注入成功，`false` 表示注入失败。
     */
    fun injectEvent(event: InputEvent, mode: InjectMode = InjectMode.ASYNC): Boolean =
        inputManager.requireMethod("injectInputEvent", InputEvent::class.java, Int::class.javaPrimitiveType)
            .callForSafe(inputManager, event, mode.code)
            ?: false
}
