package net.bi4vmr.tool.base.list

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.bi4vmr.tool.databinding.PrivacyListItemBinding

/**
 * TODO 添加简述。
 *
 * TODO 添加详情。
 *
 * @author 詹屹罡。
 */
class ListAdapterKT(context: Context) : RecyclerView.Adapter<ListAdapterKT.BaseViewHolder>() {

    companion object {
        private val TAG: String = ListAdapterKT::class.java.simpleName
    }

    private val mContext: Context = context.applicationContext

    // 数据源
    private val dataSource: MutableList<PrivacyVO> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<PrivacyVO>) {
        dataSource.clear()
        dataSource.addAll(data)
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
        val data: PrivacyVO = dataSource[holder.adapterPosition]
        holder.bindData(data)
    }

    private inner class PrivacyVH(private val binding: PrivacyListItemBinding) : BaseViewHolder(binding) {

        override fun bindData(data: PrivacyVO) {
            with(binding) {
                tvTitle.text = data.title
                tvPackageName.text = data.privacy.packageName
                tvOPName.text = data.privacy.opName
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
        abstract fun bindData(data: PrivacyVO)
    }
}
