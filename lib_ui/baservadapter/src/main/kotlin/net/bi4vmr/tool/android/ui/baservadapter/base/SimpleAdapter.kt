package net.bi4vmr.tool.android.ui.baservadapter.base

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * RecyclerView适配器的通用封装（单一表项类型）。
 *
 * 参考 [AsyncListDiffer] 实现了异步更新功能，并且也支持同步更新指定的表项。
 *
 * 泛型 [I] 表示与ViewHolder绑定的表项数据类型，必须是 [ListItem] 的子类。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
abstract class SimpleAdapter<I : ListItem>
@JvmOverloads constructor(

    /**
     * 表项所使用的布局资源ID。
     */
    @LayoutRes
    private val layoutID: Int,

    /**
     * ViewHolder的Class。
     */
    private val viewHolderClass: Class<out BaseViewHolder<I>>,

    /**
     * 初始数据源。
     */
    dataSource: MutableList<I> = mutableListOf(),

    /**
     * 后台任务的协程环境。
     *
     * 用于执行差异对比、异步更新等任务，默认值为 `Default` 调度器构建的作用域。
     */
    bgScope: CoroutineScope = CoroutineScope(Dispatchers.Default),

    /**
     * 前台任务的协程环境。
     *
     * 用于更新界面，默认值为 `Main` 调度器构建的作用域。
     *
     * 此参数仅供单元测试场景使用，其他场景下调用者无需自行传入协程环境。
     */
    uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : BaseAdapter<I>(dataSource, bgScope, uiScope) {

    /**
     * 默认的ViewHolder创建实现。
     *
     * @param[parent] 父容器。
     * @param[viewType] 表项类型代码。
     * @return ViewHolder实例。
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<I> {
        if (debugMode) {
            Log.v(tag, "OnCreateViewHolder. ViewType:[$viewType]")
        }

        // 只支持单一表项类型时，使用构造方法传入的ViewHolder类型即可，无需再查询映射表。
        val itemView = LayoutInflater.from(parent.context).inflate(layoutID, parent, false)

        // 通过反射调用ViewHolder的构造方法创建实例
        var instance: BaseViewHolder<I>
        try {
            val constructor = viewHolderClass.getConstructor(View::class.java)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(itemView) as BaseViewHolder<I>
        } catch (e: NoSuchMethodException) {
            // 以上方式仅适用于ViewHolder不是Adapter内部类的情况，如果ViewHolder在Adapter内部，构造方法第一参数会变为Adapter实例。
            val constructor = viewHolderClass.getConstructor(javaClass, View::class.java)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(this, itemView) as BaseViewHolder<I>
        }

        return instance
    }
}
