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

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
