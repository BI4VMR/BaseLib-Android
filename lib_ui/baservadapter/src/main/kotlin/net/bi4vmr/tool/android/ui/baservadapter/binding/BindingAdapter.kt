package net.bi4vmr.tool.android.ui.baservadapter.binding

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseAdapter
import net.bi4vmr.tool.android.ui.baservadapter.base.BaseViewHolder
import net.bi4vmr.tool.android.ui.baservadapter.base.ListItem
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * RecyclerView适配器的通用封装（ViewBinding支持）。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
abstract class BindingAdapter<I : ListItem>
@JvmOverloads constructor(

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
     * ViewType映射表。
     *
     * 配置ViewType、ViewBinding与ViewHolder的映射关系，以便 [onCreateViewHolder] 方法自动创建ViewHolder实例。
     *
     * 如果调用者不希望使用本工具内置的映射方案，也可以自行重写 [onCreateViewHolder] 方法。
     */
    private val bindingMappers: MutableMap<Int, Pair<Class<out ViewBinding>, Class<out BindingViewHolder<*, *>>>> =
        ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<*, I> {
        if (debugMode) {
            Log.d(tag, "OnCreateViewHolder. ViewType:[$viewType]")
        }

        val (vbClass, vhClass) = bindingMappers[viewType]
            ?: throw IllegalArgumentException("ViewType [$viewType] is unknown! Did you forget to register it?")

        // 反射调用ViewBinding的 `inflate` 方法创建ViewBinding实例
        val inflateMethod: Method = vbClass.getMethod(
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
            val constructor = vhClass.getConstructor(vbClass)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(binding) as BindingViewHolder<*, I>
        } catch (e: NoSuchMethodException) {
            // 以上方式仅适用于ViewHolder不是Adapter内部类的情况，如果ViewHolder在Adapter内部，构造方法第一参数会变为Adapter实例。
            val constructor = vhClass.getConstructor(javaClass, vbClass)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            instance = constructor.newInstance(this, binding) as BindingViewHolder<*, I>
        }

        return instance
    }

    /**
     * 注册ViewType、ViewBinding和ViewHolder的映射关系。
     *
     * @param[viewType] ViewType。
     * @param[bindingClass] ViewBinding的Class。
     * @param[viewHolderClass] ViewHolder的Class。
     */
    fun addBindingMapper(
        viewType: Int,
        bindingClass: Class<out ViewBinding>,
        viewHolderClass: Class<out BindingViewHolder<*, *>>
    ) {
        bindingMappers[viewType] = bindingClass to viewHolderClass
    }

    /**
     * 注销ViewType、ViewBinding和ViewHolder的映射关系。
     *
     * @param[viewType] ViewType。
     */
    fun removeBindingMapper(viewType: Int) {
        bindingMappers.remove(viewType)
    }

    /**
     * 获取所有ViewType、ViewBinding和ViewHolder的映射关系。
     *
     * @return 映射关系集合。
     */
    fun getBindingMappers(): Map<Int, Pair<Class<out ViewBinding>, Class<out BaseViewHolder<*>>>> {
        return bindingMappers.toMap()
    }
}
