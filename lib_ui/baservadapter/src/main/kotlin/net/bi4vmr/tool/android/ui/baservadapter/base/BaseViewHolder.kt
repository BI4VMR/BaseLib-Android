package net.bi4vmr.tool.android.ui.baservadapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseViewHolder.Companion.hasFlag

/**
 * ViewHolder的通用封装。
 *
 * 泛型 [I] 表示与ViewHolder绑定的表项数据类型，必须是 [ListItem] 的子类。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
abstract class BaseViewHolder<I : ListItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        /**
         * 判断Payload中指定Flag是否被置位。
         *
         * @param[payload] Payload。
         * @param[flag] 感兴趣的Flag。
         * @return `true` 表示Payload中包含指定Flag，`false` 表示不包含。
         * @see[BaseDiffer.addFlag]
         */
        fun hasFlag(payload: Int, flag: Int): Boolean {
            return (payload and flag) != 0
        }
    }

    /**
     * 根据表项数据刷新所有控件。
     *
     * @param[item] 新的表项数据。
     */
    abstract fun bindData(item: I)

    /**
     * 根据Payload与表项数据刷新部分控件。
     *
     * 如果使用内置的Payload机制，请使用 [hasFlag] 方法判断Payload中包含的项目。
     *
     * @param[item] 新的表项数据。
     * @param[payload] Payload。
     */
    open fun bindData(item: I, payload: Int) {
        // 默认不支持局部刷新，子类应当自行实现相关逻辑。
        bindData(item)
    }
}
