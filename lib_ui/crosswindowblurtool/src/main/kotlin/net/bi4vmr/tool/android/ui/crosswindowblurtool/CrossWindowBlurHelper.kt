package net.bi4vmr.tool.android.ui.crosswindowblurtool

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewRootImpl
import android.view.WindowManager
import com.android.internal.graphics.drawable.BackgroundBlurDrawable
import java.lang.reflect.Method
import java.util.function.Consumer

/**
 * 跨窗口模糊背景创建工具。
 *
 * 注意：该工具只能在Android 12及更高版本的系统中使用。
 *
 * 该工具使用了隐藏接口，调用者需要具有系统签名，或使用 [FreeReflection](https://github.com/tiann/FreeReflection) 等工具解除反射限制。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
@SuppressLint("BlockedPrivateApi, DiscouragedPrivateApi")
class CrossWindowBlurHelper(
    private val context: Context
) {

    companion object {

        private const val TAG: String = "CrossWindowBlurHelper"

        private const val BLUR_RADIUS_DEFAULT: Int = 32

        private const val CORNER_RADIUS_DEFAULT: Float = 32.0F

        /**
         * 通过反射获取ViewRootImpl实例。
         *
         * 仅当View已经Attach到Window上才能获取到实例，调用者可以通过[View.addOnAttachStateChangeListener]方法进行监听。
         *
         * @param[view] View实例。
         * @return ViewRootImpl实例。
         */
        private fun getViewRootImplByReflect(view: View): ViewRootImpl? {
            var viewRootImpl: ViewRootImpl? = null

            runCatching {
                val method: Method = View::class.java.getDeclaredMethod("getViewRootImpl")
                val result: Any? = method.invoke(view)
                if (result is ViewRootImpl) {
                    viewRootImpl = result
                }
            }.onFailure {
                Log.e(TAG, "Reflect operate failed! Reason:[${it.message}]")
                it.printStackTrace()
            }

            return viewRootImpl
        }

        /**
         * 通过反射获取BackgroundBlurDrawable实例。
         *
         * @param[viewRootImpl] ViewRootImpl实例。
         * @return BackgroundBlurDrawable实例。
         */
        private fun createBlurBackgroundByReflect(viewRootImpl: ViewRootImpl): BackgroundBlurDrawable? {
            var drawable: BackgroundBlurDrawable? = null

            runCatching {
                val method = ViewRootImpl::class.java.getDeclaredMethod("createBackgroundBlurDrawable")
                method.isAccessible = true
                val result: Any? = method.invoke(viewRootImpl)
                if (result is BackgroundBlurDrawable) {
                    drawable = result
                }
            }.onFailure {
                Log.e(TAG, "Reflect operate failed! Reason:[${it.message}]")
                it.printStackTrace()
            }

            return drawable
        }
    }

    private val windowManager: WindowManager by lazy {
        context.applicationContext.getSystemService(WindowManager::class.java)
    }
    private val viewAttachListener by lazy { ViewAttachListener() }
    private val windowBlurListener by lazy { WindowBlurListener() }

    private var mView: View? = null
    private var mRawBackgroundDrawable: Drawable? = mView?.background
    private var mBlurDrawable: Drawable? = null
    private var mBlurRadius: Int = BLUR_RADIUS_DEFAULT
    private var mCornerRadius: Float = CORNER_RADIUS_DEFAULT
    private var mBlurColor: Int = -1
    private var mBlurOverlay: Boolean = true

    constructor(
        view: View,
        blurRadius: Int = BLUR_RADIUS_DEFAULT,
        cornerRadius: Float = CORNER_RADIUS_DEFAULT,
        blurColor: Int = -1,
        blurOverlay: Boolean = true
    ) : this(view.context) {
        mBlurRadius = blurRadius
        mCornerRadius = cornerRadius
        mBlurColor = blurColor
        mBlurOverlay = blurOverlay
    }

    fun init(view: View) {
        release()

        mView = view
        if (view.isAttachedToWindow) {
            initWhenViewAttached()
        }

        view.addOnAttachStateChangeListener(viewAttachListener)
    }

    fun release() {
        windowManager.removeCrossWindowBlurEnabledListener(windowBlurListener)
        mView?.removeOnAttachStateChangeListener(viewAttachListener)
        if (mBlurOverlay && mRawBackgroundDrawable != null) {
            mView?.background = mRawBackgroundDrawable
        } else {
            mView?.background = null
        }
        mView = null


    }

    private fun getBlurBackground(): Drawable? {
        val view = mView
        if (view == null) {
            Log.w(TAG, "Target view is null!")
            return null
        }

        // 获取ViewRootImpl
        val viewRootImpl: ViewRootImpl? = getViewRootImplByReflect(view)
        if (null == viewRootImpl) {
            Log.e(TAG, "Get ViewRootImpl failed!")
            return null
        }

        // 获取模糊背景Drawable实例
        val drawable: BackgroundBlurDrawable? = createBlurBackgroundByReflect(viewRootImpl)
        if (drawable == null) {
            Log.e(TAG, "Create BlurBackground failed!")
            return null
        }

        drawable.setBlurRadius(mBlurRadius)
        drawable.setCornerRadius(mCornerRadius)
        if (mBlurColor != -1) {
            drawable.setColor(mBlurColor)
        }
        return drawable
    }

    // 设置模糊效果
    private fun initWhenViewAttached() {
        windowManager.addCrossWindowBlurEnabledListener(windowBlurListener)

        val sysEnable: Boolean = windowManager.isCrossWindowBlurEnabled
        Log.d(TAG, "InitWhenViewAttached. BlurEnable:[$sysEnable]")
        switchBlurState(sysEnable)
    }

    // 切换模糊效果状态
    private fun switchBlurState(enable: Boolean) {
        if (enable) {
            mBlurDrawable = getBlurBackground()
            // 判断是否需要叠加原始背景
            if (mBlurOverlay && mRawBackgroundDrawable != null) {
                val layerDrawable = LayerDrawable(arrayOf(mBlurDrawable, mRawBackgroundDrawable))
                mView?.background = layerDrawable
            } else {
                mView?.background = mBlurDrawable
            }
        } else {
            mView?.background = null
        }
    }

    /**
     * 监听View是否已被Attach。
     */
    private inner class ViewAttachListener : OnAttachStateChangeListener {

        override fun onViewAttachedToWindow(v: View) {
            Log.d(TAG, "OnViewAttachedToWindow.")
            initWhenViewAttached()
        }

        override fun onViewDetachedFromWindow(v: View) {
            Log.d(TAG, "OnViewDetachedFromWindow.")
            windowManager.removeCrossWindowBlurEnabledListener(windowBlurListener)

            if (mBlurOverlay && mRawBackgroundDrawable != null) {
                v.background = mRawBackgroundDrawable
            } else {
                v.background = null
            }
        }
    }

    /**
     * 系统模糊效果状态监听器。
     *
     * 系统进入低功耗等模式可能会禁用模糊效果，此时应当清除模糊背景。
     */
    private inner class WindowBlurListener : Consumer<Boolean> {

        override fun accept(enable: Boolean) {
            Log.d(TAG, "OnWindowBlurStateChange. State:[$enable]")
            switchBlurState(enable)
        }
    }
}
