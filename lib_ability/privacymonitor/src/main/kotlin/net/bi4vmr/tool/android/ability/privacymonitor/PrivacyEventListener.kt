package net.bi4vmr.tool.android.ability.privacymonitor

/**
 * 敏感权限事件监听器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
interface PrivacyEventListener {

    /**
     * 正在使用敏感权限的应用列表改变事件。
     *
     * @param[items] 应用信息。
     */
    fun onListChange(items: List<PrivacyItem>) {
        // 子类应当重写本方法。
    }

    /**
     * 敏感权限使用状态改变事件。
     *
     * @param[state] 使用状态。
     */
    fun onStateChange(state: Boolean) {
        // 子类应当重写本方法。
    }
}
