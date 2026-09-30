package net.bi4vmr.tool.base

import android.view.View
import android.widget.TextView
import net.bi4vmr.tool.R
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseViewHolder

/**
 * ViewHolder：类型一。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class Type1VH(
    itemView: View
) : BaseViewHolder<Type1VO>(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvContent: TextView = itemView.findViewById(R.id.tvContent)

    override fun bindData(item: Type1VO) {
        tvTitle.text = item.title
        tvContent.text = item.content
    }
}
