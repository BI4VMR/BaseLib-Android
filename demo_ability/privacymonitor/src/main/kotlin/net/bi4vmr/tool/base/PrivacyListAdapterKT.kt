package net.bi4vmr.tool.base

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.bi4vmr.tool.android.ability.framework.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.PrivacyItem
import net.bi4vmr.tool.databinding.PrivacyListItemBinding
import java.util.concurrent.CopyOnWriteArrayList

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
    private val dataSource: MutableList<PrivacyItem> = CopyOnWriteArrayList()
    // 数据源更新任务
    private var updateJob: Job? = null

    fun updateData(data: List<PrivacyItem>) {
        updateJob?.cancel()
        updateJob = CoroutineScope(Dispatchers.Default).launch {
            val result: DiffResult = DiffUtil.calculateDiff(DiffCallback(dataSource, data))
            dataSource.clear()
            dataSource.addAll(data)

            withContext(Dispatchers.Main) {
                result.dispatchUpdatesTo(this@PrivacyListAdapterKT)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        dataSource.clear()
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        Log.i(TAG, "OnDetachedFromRecyclerView.")
        updateJob?.cancel()
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
                tvOPName.text = AppOps.valueOf(data.op.opCode)?.name ?: "-"
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

    /**
     * DiffUtil比较回调。
     */
    private inner class DiffCallback(
        private val oldDatas: List<PrivacyItem>,
        private val newDatas: List<PrivacyItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldDatas.size
        }

        override fun getNewListSize(): Int {
            return newDatas.size
        }

        // 判断新旧位置的表项是否相同
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldDatas[oldItemPosition]
            val newItem = newDatas[newItemPosition]
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // 每个表项都是一个整体，不需要局部刷新。
            return true
        }
    }
}
