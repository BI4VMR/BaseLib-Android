package net.bi4vmr.tool.base

import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem

/**
 * TODO 添加简述。
 *
 * TODO 添加详情。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
data class Type2VO(
    val title: String,
    val content: String
) : ListItem {

    override fun getViewType(): Int {
        return ViewTypes.TYPE2.ordinal
    }
}
