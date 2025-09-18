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

        const val FLAG_NAME: Int = 0x1
        const val FLAG_TITLE: Int = FLAG_NAME shl 1
        const val FLAG_CONTENT: Int = FLAG_NAME shl 2
        const val FLAG_DESCRIPTION: Int = FLAG_NAME shl 3
        const val FLAG_ICON: Int = FLAG_NAME shl 4

        /**
         * 自定义标志位的起始值。
         *
         * 自定义标志位从该数值左移1位开始，依次递增。
         */
        const val FLAG_CUSTOM: Int = FLAG_ICON

        /**
         * 向Payload中添加标志位。
         *
         * @param[payload] 现有的标志位。
         * @param[flag] 待添加的Flag。
         * @return 组合后的标志位。
         */
        fun addFlag(payload: Int, flag: Int): Int {
            return payload or flag
        }
    }

    /**
     * 判断参数所指定的两个位置对应表项是否相同。
     * <p>
     * 如果表项具有唯一ID或多种类型，可以加入判断逻辑。
     *
     * @param[oldItem] 旧数据源中的表项。
     * @param[newItem] 新数据源中的表项。
     * @return 值为 `false` 时，表示两个表项不同；值为 `true` 时表示两个表项是相同的，但内部数据可能有变化，随后会调用
     * `areContentsTheSame()` 方法判断表项中的数据是否需要更新。
     */
    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * 判断参数所指定的两个位置对应表项的内容是否相同。
     * <p>
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
     * @return 组合标志位，通过 [FLAG_NAME] 等标志位指明需要更新的UI组件。
     */
    abstract fun getChangePayload(oldItem: T, newItem: T): Int
}
