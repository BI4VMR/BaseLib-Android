package net.bi4vmr.tool.base

import net.bi4vmr.tool.R
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseAdapter
import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem

/**
 * 基础适配器。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class MyAdapter : BaseAdapter<ListItem>() {

    // 注册表项类型、布局文件、ViewHolder的映射关系
    init {
        addViewTypeMapper(ViewTypes.TYPE1.ordinal, R.layout.list_item_type1, Type1VH::class.java)
        addViewTypeMapper(ViewTypes.TYPE2.ordinal, R.layout.list_item_type2, Type2VH::class.java)
    }
}
