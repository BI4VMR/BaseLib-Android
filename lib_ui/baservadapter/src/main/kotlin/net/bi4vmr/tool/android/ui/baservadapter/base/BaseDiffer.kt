package net.bi4vmr.tool.android.ui.baservadapter.base

/**
 * DiffUtil比较回调基本封装。
 *
 * Payload为多个二进制位经过或运算组成的标志位，每个二进制位对应一组UI元素，例如： `0x1` 表示标题、 `0x2` 表示描述，如果标题与描述都需要
 * 更新，则应当在 [getChangePayload] 方法中返回 `0x3` ；并在对应ViewHolder的 [BaseViewHolder.bindData] 方法中实现View更新逻辑。
 *
 * 本类提供了一些常用的标志位定义，如果无法满足实际需要，可以基于 [FLAG_CUSTOM] 定义新的标志位。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
abstract class BaseDiffer<T : ListItem> {

    companion object {

        /**
         * 标志位起始值。
         */
        private const val FLAG_START: Int = 0x1

        /**
         * 内置标志位：设置表项点击事件监听器。
         */
        const val FLAG_PRIVATE_CLICK_LISTENER_SET: Int = FLAG_START

        /**
         * 内置标志位：撤销表项点击事件监听器。
         */
        const val FLAG_PRIVATE_CLICK_LISTENER_UNSET: Int = FLAG_START shl 1

        /**
         * 预设标志位：名称。
         */
        const val FLAG_NAME: Int = FLAG_START shl 2

        /**
         * 预设标志位：标题。
         */
        const val FLAG_TITLE: Int = FLAG_START shl 3

        /**
         * 预设标志位：内容。
         */
        const val FLAG_CONTENT: Int = FLAG_START shl 4

        /**
         * 预设标志位：描述。
         */
        const val FLAG_DESCRIPTION: Int = FLAG_START shl 5

        /**
         * 预设标志位：图标。
         */
        const val FLAG_ICON: Int = FLAG_START shl 6

        /**
         * 预设标志位：状态。
         */
        const val FLAG_STATE: Int = FLAG_START shl 7

        /**
         * 预设标志位：可用性。
         */
        const val FLAG_AVAILABLE: Int = FLAG_START shl 8

        /**
         * 自定义标志位的起始值。
         *
         * 自定义标志位从最大的预设标志位数值左移 `1` 位开始，依次递增。
         */
        const val FLAG_CUSTOM: Int = FLAG_AVAILABLE

        /**
         * 向Payload中添加标志位。
         *
         * @param[payload] 现有的标志位。
         * @param[flag] 待添加的Flag。
         * @return 组合后的标志位。
         * @see[BaseViewHolder.hasFlag]
         */
        fun addFlag(payload: Int, flag: Int): Int {
            return payload or flag
        }
    }

    /**
     * 判断参数所指定的两个位置对应表项类型是否相同。
     *
     * @param[oldItem] 旧数据源中的表项。
     * @param[newItem] 新数据源中的表项。
     * @return 值为 `false` 时，表示两个表项不同；值为 `true` 时表示两个表项是相同的，但内部数据可能有变化，随后会调用
     * `areContentsTheSame()` 方法判断表项中的数据是否需要更新。
     */
    open fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // 默认实现：比较ViewType，如果ViewType不同，则两个表项一定是不同的。
        val viewTypeSame = oldItem.getViewType() == newItem.getViewType()
        return viewTypeSame
    }

    /**
     * 判断参数所指定的两个位置对应表项内容是否相同。
     *
     * 仅当 `areItemsTheSame()` 返回 `true` 时才会调用本方法。
     *
     * @param[oldItem] 旧数据源中的表项。
     * @param[newItem] 新数据源中的表项。
     * @return 值为 `true` 时表示两个表项内容相同，值为 `false` 表示两个表项内容不同。
     */
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * 计算发生变化的属性，使得适配器执行局部刷新。
     *
     * @param[oldItem] 旧数据源中的表项。
     * @param[newItem] 新数据源中的表项。
     * @return 组合标志位，可以通过 [FLAG_NAME] 等标志位指明需要更新的UI组件。
     */
    open fun getChangePayload(oldItem: T, newItem: T): Any = 0
}
