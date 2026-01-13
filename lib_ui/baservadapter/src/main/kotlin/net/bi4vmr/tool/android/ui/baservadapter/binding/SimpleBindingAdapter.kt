package net.bi4vmr.tool.android.ui.baservadapter.binding

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseAdapter
import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem
import java.lang.reflect.Method

/**
 * RecyclerView适配器的通用封装（单一表项类型与ViewBinding支持）。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
@Suppress("UNCHECKED_CAST")
abstract class SimpleBindingAdapter<I : ListItem>
@JvmOverloads constructor(

    /**
     * ViewBinding的Class。
     */
    private val viewBindingClass: Class<out ViewBinding>,

    /**
     * ViewHolder的Class。
     */
    private val viewHolderClass: Class<out BindingViewHolder<out ViewBinding, out ListItem>>,

    /**
     * 初始数据源。
     */
    dataSource: MutableList<I> = mutableListOf(),

    /**
     * 后台任务的协程调度器。
     *
     * 用于执行差异对比、异步更新等任务，默认值为 [Dispatchers.Default] 。
     */
    bgDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseAdapter<I>(dataSource, bgDispatcher) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*, I> {
        if (debugMode) {
            Log.d(tag, "OnCreateViewHolder. ViewType:[$viewType]")
        }

        // 只支持单一表项类型，使用泛型指定的ViewHolder即可，无需再查询映射表。
        // 反射调用 [ViewBinding.inflate] 方法创建ViewBinding实例
        val inflateMethod: Method = viewBindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        val binding = inflateMethod.invoke(null, LayoutInflater.from(parent.context), parent, false)
            ?: throw IllegalStateException("Invoke ViewBinding.inflate() failed!")

        // 通过反射调用ViewHolder的构造方法创建实例
        var instance: BindingViewHolder<*, I>
        try {
            val constructor = viewHolderClass.getConstructor(viewBindingClass)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(binding) as BindingViewHolder<*, I>
        } catch (e: NoSuchMethodException) {
            // 以上方式仅适用于ViewHolder不是Adapter内部类的情况，如果ViewHolder在Adapter内部，构造方法第一参数会变为Adapter实例。
            val constructor = viewHolderClass.getConstructor(javaClass, viewBindingClass)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(this, binding) as BindingViewHolder<*, I>
        }

        return instance
    }
}
