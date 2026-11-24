package net.bi4vmr.tool.android.ability.framework.view

import android.os.Build
import android.view.SurfaceControl
import androidx.annotation.RequiresApi

/**
 * [SurfaceControl] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object SurfaceControlExtend {

    /**
     * 创建实例。
     *
     * @return 新的实例。
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun newInstance(): SurfaceControl =
        SurfaceControl::class.java.getDeclaredConstructor().newInstance()
}
