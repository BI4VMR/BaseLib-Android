package net.bi4vmr.tool.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.bi4vmr.tool.android.ability.privacy.PrivacyItem
import net.bi4vmr.tool.databinding.PrivacyListItemBinding

/**
 * 隐私应用列表适配器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class PrivacyListAdapterKT : RecyclerView.Adapter<PrivacyListAdapterKT.BaseViewHolder>() {

    companion object {
        private val TAG: String = "TestApp-${PrivacyListAdapterKT::class.java.simpleName}"
    }

    // 数据源
    private val dataSource: MutableList<PrivacyItem> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<PrivacyItem>) {
        dataSource.clear()
        dataSource.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        dataSource.clear()
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = PrivacyListItemBinding.inflate(inflater, parent, false)
        return PrivacyVH(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data: PrivacyItem = dataSource[holder.adapterPosition]
        holder.bindData(data)
    }

    /**
     * 隐私应用表项ViewHolder。
     */
    private inner class PrivacyVH(private val binding: PrivacyListItemBinding) : BaseViewHolder(binding) {

        override fun bindData(data: PrivacyItem) {
            with(binding) {
                tvTitle.text = data.title
                tvPackageName.text = data.op.packageName
                tvOPName.text = data.op.opName
                ivIcon.setImageDrawable(data.icon)
            }
        }
    }

    /**
     * 基础ViewHolder，用于定义一些公共方法。
     */
    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // 构造方法：使用ViewBinding进行初始化
        constructor(binding: ViewBinding) : this(binding.root)

        // 将数据与ViewHolder绑定
        abstract fun bindData(data: PrivacyItem)
    }
}
