package net.bi4vmr.tool.android.ui.baservadapter.base;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * 表项点击事件监听器（Java环境）。
 * <p>
 * 与 {@link BaseAdapter.ItemClickListener} 功能一致，使用默认实现重写了"onItemLongClick"方法，解决Java环境不识别Kotlin接口默认
 * 实现导致无法使用Lambda表达式的问题。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
public interface ItemClickListenerJava<I> extends BaseAdapter.ItemClickListener<I> {

    @Override
    default boolean onItemLongClick(int position, I item, @NonNull View view) {
        return true;
    }
}
