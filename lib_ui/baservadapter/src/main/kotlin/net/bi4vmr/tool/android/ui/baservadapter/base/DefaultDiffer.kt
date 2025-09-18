package net.bi4vmr.tool.android.ui.baservadapter.base

import android.annotation.SuppressLint

/**
 * 默认的DiffUtil比较回调实现。
 *
 * 根据ViewType和 `equals()` 方法比较两个表项是否相同，且不支持局部刷新。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
internal class DefaultDiffer<T : ListItem> : BaseDiffer<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // 首先比较ViewType，如果ViewType不同，则两个表项一定是不同的。
        val viewTypeSame = oldItem.getViewType() == newItem.getViewType()
        if (!viewTypeSame) return false

        // 如果ViewType相同，则调用两个表项的 `equals()` 方法进行比较。
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: T, newItem: T): Int = 0
}
