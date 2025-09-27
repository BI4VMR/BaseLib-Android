package net.bi4vmr.tool.android.ui.baservadapter.base

import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * RecyclerView适配器的通用封装。
 *
 * 参考 [AsyncListDiffer] 实现了异步更新功能，并且也支持同步更新指定的表项。
 *
 * 泛型 [I] 表示与ViewHolder绑定的表项数据类型，必须是 [ListItem] 的子类。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
abstract class BaseAdapter<I : ListItem>
@JvmOverloads constructor(

    /**
     * 内部数据源。
     */
    private val mDataSource: MutableList<I> = CopyOnWriteArrayList(),

    /**
     * 后台任务的协程环境。
     *
     * 用于执行差异对比、异步更新等任务，默认值为 `Default` 调度器构建的作用域。
     */
    private val bgScope: CoroutineScope = CoroutineScope(Dispatchers.Default),

    /**
     * 前台任务的协程环境。
     *
     * 用于更新界面，默认值为 `Main` 调度器构建的作用域。
     *
     * 此参数仅供单元测试场景使用，其他场景下调用者无需自行传入协程环境。
     */
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : RecyclerView.Adapter<BaseViewHolder<I>>() {

    companion object {
        private val DEFAULT_DEBOUNCE_DURATION = 500L
    }

    /**
     * 日志Tag。
     */
    protected open val tag: String = javaClass.simpleName

    /**
     * 调试模式开关。
     *
     * 用于控制是否输出详细日志。
     */
    var debugMode: Boolean = false

    /**
     * ViewType映射表。
     *
     * 配置ViewType、Layout资源ID与ViewHolder的映射关系，以便 [onCreateViewHolder] 方法自动创建ViewHolder实例。
     *
     * 如果调用者不希望使用本工具内置的映射方案，也可以自行重写 [onCreateViewHolder] 方法。
     */
    private val viewTypeMappers: MutableMap<Int, Pair<Int, Class<out BaseViewHolder<*>>>> = ConcurrentHashMap()

    /**
     * 当前Adapter所绑定的RecyclerView。
     */
    private var mRecyclerView: RecyclerView? = null

    /**
     * 表项点击事件监听器实现。
     */
    private var mItemClickListener: ItemClickListener? = null

    /**
     * 点击事件防抖时长。
     *
     * 默认值为500毫秒。
     */
    private var mDebounceDuration: Long = DEFAULT_DEBOUNCE_DURATION

    /**
     * DiffUtil比较回调。
     */
    private var mDiffCallback: BaseDiffer<I> = DefaultDiffer()

    /**
     * 最新的异步更新任务序号。
     *
     * 每个异步任务开始时将该序号加 `1` 并保存在自身的线程中，差异对比完毕后与全局变量比较：
     *
     * 如果任务序号与全局变量相同，说明任务有效，可以更新列表；否则说明已经有更晚开始的任务更新了列表，当前任务没必要再更新列表。
     */
    private var mUpdateTaskSequence: Int = 0

    /**
     * 数据更新互斥锁。
     *
     * 确保同时只能有一个协程访问数据源。
     *
     * 预留，暂不使用，目前更新数据均在主线程调度器执行，不会出现数据竞争问题。
     */
    private val updateMutex: Mutex? = null

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (debugMode) {
            Log.d(tag, "OnAttachedToRecyclerView.")
        }

        mRecyclerView = recyclerView
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (debugMode) {
            Log.d(tag, "OnDetachedFromRecyclerView.")
        }

        mRecyclerView = null
    }

    /**
     * 默认的ViewHolder创建实现。
     *
     * @param[parent] 父容器。
     * @param[viewType] 表项类型代码。
     * @return ViewHolder实例。
     */
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<I> {
        if (debugMode) {
            Log.d(tag, "OnCreateViewHolder. ViewType:[$viewType]")
        }

        val (layoutID, vhClass) = viewTypeMappers[viewType]
            ?: throw IllegalArgumentException("ViewType [$viewType] is unknown! Did you forget to register it?")
        // 通过布局文件创建View实例
        val itemView = LayoutInflater.from(parent.context).inflate(layoutID, parent, false)
        // 通过反射调用ViewHolder的构造方法创建实例
        val constructor = vhClass.getConstructor(View::class.java)
        return constructor.newInstance(itemView) as BaseViewHolder<I>
    }

    /**
     * 默认的数据绑定实现。
     *
     * @param[holder] 待绑定的ViewHolder实例。
     * @param[position] 表项索引序号。
     */
    override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int) {
        if (debugMode) {
            Log.d(tag, "OnBindViewHolder. Position:[$position]")
        }

        val item: I = mDataSource[position]

        // 注册表项点击监听器
        mItemClickListener?.let { outListener ->
            holder.itemView.setDebouncedClickListener(tag, mDebounceDuration) {
                outListener.onItemClick(holder.adapterPosition, item, it)
            }
            holder.itemView.setOnLongClickListener {
                outListener.onItemLongClick(holder.adapterPosition, item, it)
            }
        }

        // 执行数据绑定逻辑
        holder.bindData(item)
    }

    /**
     * 默认的数据绑定实现（局部刷新）。
     *
     * 使用本工具内置的Payload机制刷新表项，详见 [BaseDiffer] 类的注释。
     *
     * 如果子类希望使用自定义Payload机制，可以重写该方法。
     *
     * @param[holder] 待绑定的ViewHolder实例。
     * @param[position] 表项索引序号。
     * @param[payloads] Payloads。
     * @see BaseDiffer
     */
    override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            // 默认选择最新的Payload，忽略旧的Payload。
            val payload = payloads.last()
            if (payload !is Int) {
                Log.w(tag, "Payload type is not a number with flags, ignored!")
                return
            }

            if (debugMode) {
                Log.d(tag, "OnBindViewHolder. Position:[$position] Payload:[${payload.toString(2)}]")
            }

            val item: I = mDataSource[position]

            // 如果是内置Flag，则执行相应的逻辑，不必通知子类。
            if (BaseViewHolder.hasFlag(payload, BaseDiffer.FLAG_PRIVATE_CLICK_LISTENER_SET)) {
                holder.itemView.let { rootView ->
                    rootView.setDebouncedClickListener(tag, mDebounceDuration) {
                        notifyItemClick(holder.adapterPosition, item, rootView)
                    }
                    rootView.setOnLongClickListener {
                        notifyItemLongClick(holder.adapterPosition, item, rootView)
                    }
                }
                return
            } else if (BaseViewHolder.hasFlag(payload, BaseDiffer.FLAG_PRIVATE_CLICK_LISTENER_UNSET)) {
                holder.itemView.apply {
                    setOnClickListener(null)
                    setOnLongClickListener(null)
                }
                return
            }

            // 非内置Flag，则执行自定义逻辑。
            holder.bindData(item, payload)
        }
    }

    /**
     * 获取表项数量。
     *
     * @return 表项数量。
     */
    override fun getItemCount(): Int {
        return mDataSource.size
    }

    /**
     * 获取表项类型。
     *
     * @param[position] 表项索引。
     * @return 表项类型代码。
     */
    override fun getItemViewType(position: Int): Int {
        val item: I = mDataSource[position]
        return item.getViewType()
    }

    /**
     * 注册ViewType、布局和ViewHolder的映射关系。
     *
     * @param[viewType] ViewType。
     * @param[layoutID] 布局XML的ID。
     * @param[viewHolderClass] ViewHolder的Class。
     */
    fun addViewTypeMapper(viewType: Int, @LayoutRes layoutID: Int, viewHolderClass: Class<out BaseViewHolder<*>>) {
        viewTypeMappers[viewType] = layoutID to viewHolderClass
    }

    /**
     * 注销ViewType、布局和ViewHolder的映射关系。
     *
     * @param[viewType] ViewType。
     */
    fun removeViewTypeMapper(viewType: Int) {
        viewTypeMappers.remove(viewType)
    }

    /**
     * 获取所有ViewType、布局和ViewHolder的映射关系。
     *
     * @return 映射关系集合。
     */
    fun getViewTypeMappers(): Map<Int, Pair<Int, Class<out BaseViewHolder<*>>>> {
        return viewTypeMappers.toMap()
    }

    /**
     * 获取数据源。
     *
     * 该方法返回的数据源即内置数据源，因此不可修改表项的属性，防止影响到列表显示。
     *
     * 如果希望修改表项而不影响列表显示，请使用 [getCopyOfDataSource] 方法获取数据源副本。
     *
     * @return 当前数据源。
     * @see [getCopyOfDataSource]
     */
    fun getDataSource(): List<I> = mDataSource

    /**
     * 获取数据源的副本。
     *
     * 有时我们需要对数据源进行一些修改，例如过滤与统计等操作，但我们又不希望影响到列表显示，此时可以使用本方法获取列表。
     *
     * 该方法依赖列表项的 [ListItem.copy] 方法实现深拷贝，如果列表项并未正确实现此方法，修改数据源仍会影响列表显示。
     *
     * @return 当前数据源的副本。
     * @see [getDataSource]
     */
    @Suppress("UNCHECKED_CAST")
    fun getCopyOfDataSource(): List<I> {
        return mDataSource.map { it.copy() as I }
    }

    /**
     * 获取指定表项。
     *
     * 该方法返回的数据源即内置数据源中的表项，因此不可修改表项的属性，防止影响到列表显示。
     *
     * 如果希望修改表项而不影响列表显示，请使用 [getCopyOfItem] 方法获取数据源副本。
     *
     * 如果给定的位置索引超出范围，则返回空值。
     *
     * @param[position] 待获取的位置。
     * @return 表项数据。
     * @see[getCopyOfItem]
     */
    fun getItem(position: Int): I? {
        return mDataSource.getOrNull(position)
    }

    /**
     * 获取指定表项的副本。
     *
     * 有时我们需要对表项进行一些修改，例如获取原表项修改属性，但我们又不希望影响到列表显示，此时可以使用本方法获取列表。
     *
     * 该方法依赖列表项的 [ListItem.copy] 方法实现深拷贝，如果列表项并未正确实现此方法，修改表项仍会影响列表显示。
     *
     * 如果给定的位置索引超出范围，则返回空值。
     *
     * @param[position] 待获取的位置。
     * @return 表项数据的副本。
     * @see[getItem]
     */
    @Suppress("UNCHECKED_CAST")
    fun getCopyOfItem(position: Int): I? {
        return mDataSource.getOrNull(position)?.copy() as? I
    }

    /**
     * 向列表末尾插入表项。
     *
     * @param[data] 新的表项。
     */
    fun addItem(data: I) {
        if (debugMode) {
            Log.d(tag, "AddItem. Data:$data")
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource.add(data)
            notifyItemInserted(mDataSource.size - 1)
        }
    }

    /**
     * 向指定位置插入表项。
     *
     * 将新的表项插入到指定位置，若该位置已存在表项，则将该表项以及后继表项都后移一位。
     *
     * @param[position] 待插入的位置。
     * @param[data]     新的表项。
     */
    fun addItem(position: Int, data: I) {
        if (debugMode) {
            Log.d(tag, "AddItem. Position:[$position] Data:$data")
        }

        if (position < 0 || position > mDataSource.size) {
            Log.w(tag, "Position [$position] is out of bounds, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource.add(position, data)
            notifyItemInserted(position)
        }
    }

    /**
     * 向列表末尾插入表项。
     *
     * @param[datas] 新的表项。
     */
    fun addItems(datas: List<I>) {
        if (debugMode) {
            if (datas.isEmpty()) {
                Log.d(tag, "AddItems. Data is empty.")
            } else {
                Log.d(tag, "AddItems. Data size is [${datas.size}], detail info start:")
                datas.forEachIndexed { i, item ->
                    Log.d(tag, "[$i] -> $item")
                }
                Log.d(tag, "AddItems. Data detail info end.")
            }
        }

        if (datas.isEmpty()) {
            Log.w(tag, "Data is empty, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            val oldSize = mDataSource.size
            mDataSource.addAll(datas)
            notifyItemRangeInserted(oldSize, mDataSource.size)
        }
    }

    /**
     * 向指定位置插入表项。
     *
     * 将新的表项插入到指定位置，若该位置已存在表项，则将该表项以及后继表项都后移一位。
     *
     * @param[position] 待插入的位置。
     * @param[datas]    新的表项。
     */
    fun addItems(position: Int, datas: List<I>) {
        if (debugMode) {
            if (datas.isEmpty()) {
                Log.d(tag, "AddItems. Data is empty.")
            } else {
                Log.d(tag, "AddItems. Data size is [${datas.size}], detail info start:")
                datas.forEachIndexed { i, item ->
                    Log.d(tag, "[$i] -> $item")
                }
                Log.d(tag, "AddItems. Data detail info end.")
            }
        }

        if (datas.isEmpty()) {
            Log.w(tag, "Data is empty, ignored!")
            return
        }

        if (position < 0 || position > mDataSource.size) {
            Log.w(tag, "Position [$position] is out of bounds, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource.addAll(position, datas)
            notifyItemRangeInserted(position, datas.size)
        }
    }

    /**
     * 移除指定的表项。
     *
     * 移除指定的表项，若该位置之后存在表项，则将这些表项都前移一位。
     *
     * @param[position] 待移除的位置。
     */
    fun removeItem(position: Int) {
        if (debugMode) {
            Log.d(tag, "RemoveItem. Position:[$position]")
        }

        if (position < 0 || position >= mDataSource.size) {
            Log.w(tag, "Position [$position] is out of bounds, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 清空所有表项。
     */
    fun clearItems() {
        if (debugMode) {
            Log.d(tag, "ClearItems.")
        }

        if (mDataSource.isEmpty()) {
            Log.w(tag, "List already empty now, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            val oldSize = mDataSource.size
            mDataSource.clear()
            notifyItemRangeRemoved(0, oldSize)
        }
    }

    /**
     * 更新指定的表项。
     *
     * @param[position] 待更新的位置。
     * @param[data]     新的表项。
     * @param[payload]  局部更新标志位，如果为空值或未指定则表示全量刷新。
     */
    fun updateItem(position: Int, data: I, payload: Any? = null) {
        if (debugMode) {
            Log.d(tag, "UpdateItem. Position:[$position] Data:$data")
        }

        if (position < 0 || position >= mDataSource.size) {
            Log.w(tag, "Position [$position] is out of bounds, ignored!")
            return
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource[position] = data
            notifyItemChanged(position, payload)
        }
    }

    /**
     * 更新所有表项。
     *
     * 使用 `notifyDataSetChanged()` 方法刷新整个列表，性能较低，如非必要请使用异步更新方法 [submit] 。
     *
     * @param[datas] 新的数据源。
     * @see[submit]
     */
    @SuppressLint("NotifyDataSetChanged")
    fun reloadItems(datas: List<I>) {
        if (debugMode) {
            if (datas.isEmpty()) {
                Log.d(tag, "ReloadItems. Data is empty.")
            } else {
                Log.d(tag, "ReloadItems. Data size is [${datas.size}], detail info start:")
                datas.forEachIndexed { i, item ->
                    Log.d(tag, "[$i] -> $item")
                }
                Log.d(tag, "ReloadItems. Data detail info end.")
            }
        }

        uiScope.launch {
            mUpdateTaskSequence++

            mDataSource.clear()
            mDataSource.addAll(datas)
            notifyDataSetChanged()
        }
    }

    /**
     * 使用DiffUtil异步更新表项。
     *
     * 默认的 [DefaultDiffer] 不支持局部刷新，调用者可以通过 [setDiffCallback] 方法设置自定义的DiffCallback实现，以支持局部刷新；若
     * 要恢复默认的 [DefaultDiffer] ，请使用 [resetDiffCallback] 方法。
     *
     * @param[newData] 新的列表。
     * @param[detectMoves] 表项移动检测功能开关，默认为开启。DiffUtil的算法检测表项是否被移动需要额外消耗性能，如果新旧表项排序规则一致，
     * 只是增删表项，可以关闭此功能以提升性能。
     * @param[actionAfterUpdate] 更新成功后需要执行的动作，将被提交到RecyclerView的事件队列中。
     * @see[setDiffCallback]
     * @see[resetDiffCallback]
     */
    @JvmOverloads
    fun submit(
        newData: List<I>,
        detectMoves: Boolean = true,
        actionAfterUpdate: (() -> Unit)? = null
    ) {

        // 提交更新成功后需要执行的动作
        fun postActionAfterUpdate() {
            mRecyclerView?.post {
                actionAfterUpdate?.invoke()
            }
        }

        val taskStartTime = SystemClock.elapsedRealtime()
        val taskSequence = ++mUpdateTaskSequence
        val oldData = mDataSource.toList()

        if (debugMode) {
            Log.d(tag, "Submit. Async task Start. TaskID:[$taskSequence]")
            if (newData.isEmpty()) {
                Log.d(tag, "Submit. New data is empty.")
            } else {
                Log.d(tag, "Submit. New data size is [${newData.size}], detail info start:")
                newData.forEachIndexed { i, item ->
                    Log.d(tag, "[$i] -> $item")
                }
                Log.d(tag, "Submit. New data detail info end.")
            }
            if (oldData.isEmpty()) {
                Log.d(tag, "Submit. Old data is empty.")
            } else {
                Log.d(tag, "Submit. Old data size is [${oldData.size}], detail info start:")
                oldData.forEachIndexed { i, item ->
                    Log.d(tag, "[$i] -> $item")
                }
                Log.d(tag, "Submit. Old data detail info end.")
            }
        }

        // 如果两个列表元素相同，则无需执行任何动作。
        if (newData == oldData) {
            Log.i(tag, "Submit. New list is same as old, nothing to do.")
            postActionAfterUpdate()
            return
        }

        // 快速处理某个列表为空的情况，无需执行差异对比。
        if (newData.isEmpty()) {
            uiScope.launch {
                val oldSize = oldData.size
                mDataSource.clear()
                notifyItemRangeRemoved(0, oldSize)
                postActionAfterUpdate()
            }
            return
        }

        if (oldData.isEmpty()) {
            uiScope.launch {
                mDataSource.addAll(newData)
                notifyItemRangeInserted(0, newData.size)
                postActionAfterUpdate()
            }
            return
        }

        // 开始计算差异
        bgScope.launch {
            val diffCallback = mDiffCallback
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int = oldData.size

                override fun getNewListSize(): Int = newData.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldData[oldItemPosition]
                    val newItem = newData[newItemPosition]
                    return diffCallback.areItemsTheSame(oldItem, newItem)
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldData[oldItemPosition]
                    val newItem = newData[newItemPosition]
                    return diffCallback.areContentsTheSame(oldItem, newItem)
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
                    val oldItem = oldData[oldItemPosition]
                    val newItem = newData[newItemPosition]
                    return diffCallback.getChangePayload(oldItem, newItem)
                }
            }, detectMoves)

            if (debugMode) {
                val usedTime = SystemClock.elapsedRealtime() - taskStartTime
                Log.d(tag, "Submit. Async task end. TaskID:[$taskSequence] Time:[$usedTime]")
            }

            uiScope.launch {
                if (taskSequence == mUpdateTaskSequence) {
                    mDataSource.clear()
                    mDataSource.addAll(newData)
                    diffResult.dispatchUpdatesTo(this@BaseAdapter)
                    // 更新完毕后执行其他任务
                    postActionAfterUpdate()
                } else {
                    Log.w(tag, "Submit. Task [$taskSequence] is not the newest, ignore!")
                }
            }
        }
    }

    /**
     * 设置DiffUtil比较回调。
     *
     * @param[callback] 回调实现。
     */
    fun setDiffCallback(callback: BaseDiffer<I>) {
        mDiffCallback = callback
    }

    /**
     * 清除DiffUtil比较回调。
     */
    fun resetDiffCallback() {
        mDiffCallback = DefaultDiffer()
    }

    /**
     * 设置带有防抖功能的点击监听器。
     *
     * @param[tag] 日志Tag。
     * @param[duration] 防抖时长。默认500毫秒。
     * @param[l] 监听器实现。
     */
    private fun View.setDebouncedClickListener(tag: String, duration: Long = 500L, l: View.OnClickListener) {
        var lastClickTime = 0L

        setOnClickListener {
            val currentTS = SystemClock.elapsedRealtime()
            val time = currentTS - lastClickTime

            // 如果当前时间和上次点击时间间隔小于防抖时长，则忽略此次点击。
            if (time < duration) {
                Log.w(tag, "Click too fast, ignored.")
                return@setOnClickListener
            }

            lastClickTime = currentTS
            l.onClick(it)
        }
    }

    /**
     * 表项点击事件监听器定义。
     */
    interface ItemClickListener {

        /**
         * 表项被点击事件。
         *
         * @param[position] 当前表项的索引。
         * @param[item] 当前表项的数据。
         * @param[view] 当前表项的视图。
         */
        fun onItemClick(position: Int, item: ListItem, view: View)

        /**
         * 表项被长按事件。
         *
         * @param[position] 当前表项的索引。
         * @param[item] 当前表项的数据。
         * @param[view] 当前表项的视图。
         * @return `true` 表示事件处理完毕无需分发给子View， `false` 表示事件需要继续分发给子View。
         */
        fun onItemLongClick(position: Int, item: ListItem, view: View): Boolean {
            // 默认忽略长按事件
            return true
        }
    }

    /**
     * 内部方法：通知外部监听器表项被点击。
     *
     * @param[position] 当前表项的索引。
     * @param[item] 当前表项的数据。
     * @param[view] 当前表项的视图。
     */
    private fun notifyItemClick(position: Int, item: ListItem, view: View) {
        mItemClickListener?.onItemClick(position, item, view)
    }

    /**
     * 内部方法：通知外部监听器表项被长按。
     *
     * @param[position] 当前表项的索引。
     * @param[item] 当前表项的数据。
     * @param[view] 当前表项的视图。
     * @return `true` 表示事件处理完毕无需分发给子View， `false` 表示事件需要继续分发给子View。
     */
    private fun notifyItemLongClick(position: Int, item: ListItem, view: View): Boolean {
        return mItemClickListener?.onItemLongClick(position, item, view) ?: true
    }

    /**
     * 设置表项点击事件监听器。
     *
     * @param[debounceDuration] 点击事件的防抖时长，默认为500毫秒。
     * @param[listener] 监听器实现，传入空值表示取消监听。
     */
    @MainThread
    @JvmOverloads
    fun setItemClickListener(listener: ItemClickListener?, debounceDuration: Long = DEFAULT_DEBOUNCE_DURATION) {
        mDebounceDuration = debounceDuration
        mItemClickListener = listener

        if (listener == null) {
            /* 参数为空，表示撤销监听器。 */
            notifyItemRangeChanged(0, itemCount, BaseDiffer.FLAG_PRIVATE_CLICK_LISTENER_UNSET)
        } else {
            /* 参数非空，表示设置监听器。 */
            notifyItemRangeChanged(0, itemCount, BaseDiffer.FLAG_PRIVATE_CLICK_LISTENER_SET)
        }
    }
}
