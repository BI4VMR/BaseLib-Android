package net.bi4vmr.tool.android.ui.baservadapter.binding

import androidx.viewbinding.ViewBinding
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseViewHolder
import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem

/**
 * ViewHolder的通用封装（支持ViewBinding）。
 *
 * 泛型 [VB] 表示与ViewHolder绑定的ViewBinding类型。
 *
 * 泛型 [I] 表示与ViewHolder绑定的表项数据类型，必须是 [ListItem] 的子类。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
abstract class BindingViewHolder<VB : ViewBinding, I : ListItem>(

    /**
     * ViewBinding实例。
     *
     * 调用者可以通过该属性访问控件。
     */
    protected val binding: VB
) : BaseViewHolder<I>(binding.root)
