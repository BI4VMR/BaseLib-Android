@file:SuppressLint("PrivateApi")

package net.bi4vmr.tool.android.ability.framework.window

import android.annotation.SuppressLint
import android.os.Build
import android.view.IWindowManager
import android.view.SurfaceControl
import androidx.annotation.RequiresApi
import net.bi4vmr.tool.kotlin.common.reflect.callFor
import net.bi4vmr.tool.kotlin.common.reflect.requireMethod

/**
 * `WindowManagerService` 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object WindowManagerServiceExtend {

    private val TAG: String = WindowManagerServiceExtend::class.java.simpleName

    private val wms: IWindowManager = getWindowManagerService()


    /**
     * 获取 `WindowManagerService` 实例。
     *
     * @return IWindowManager。
     */
    fun getWindowManagerService(): IWindowManager =
        Class.forName("android.view.WindowManagerGlobal")
            .getMethod("getWindowManagerService")
            .invoke(null) as IWindowManager

    /**
     * 创建显示屏镜像。
     *
     * @param[displayID] 目标显示器 ID。
     * @param[surfaceControl] SurfaceControl，用于接收镜像画面。
     * @return 是否创建成功。
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun mirrorDisplay(displayID: Int, surfaceControl: SurfaceControl): Boolean {
        val result = wms
            .requireMethod("mirrorDisplay", Int::class.javaPrimitiveType, SurfaceControl::class.java)
            .callFor<Boolean>(wms, displayID, surfaceControl)
        return result ?: false
    }
}
