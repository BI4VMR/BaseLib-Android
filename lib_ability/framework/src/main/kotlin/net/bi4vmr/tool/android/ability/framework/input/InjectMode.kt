package net.bi4vmr.tool.android.ability.framework.input

/**
 * 事件注入模式。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
enum class InjectMode(

    /**
     * 数字编码。
     */
    val code: Int
) {

    /**
     * 交互事件注入模式：不检查事件分发情况。
     *
     * `injectInputEvent()` 方法将立刻返回 `true`，不阻塞调用线程。
     *
     * 详见 `InputManager.INJECT_INPUT_EVENT_MODE_ASYNC`。
     */
    ASYNC(0),

    /**
     * 交互事件注入模式：等待事件分发至接收者。
     *
     * 详见 `InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT`。
     */
    WAIT_FOR_RESULT(1),

    /**
     * 交互事件注入模式：等待接收者处理完毕。
     *
     * 直到接收者处理完毕，`injectInputEvent()` 方法才会返回，通常用于需要严格控制顺序的自动化测试场景。
     *
     * 详见 `InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH`。
     */
    WAIT_FOR_FINISH(2);
}
