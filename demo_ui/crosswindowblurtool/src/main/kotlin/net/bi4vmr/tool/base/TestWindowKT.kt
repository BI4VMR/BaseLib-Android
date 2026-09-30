package net.bi4vmr.tool.base

import android.app.Dialog
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import net.bi4vmr.tool.R
import net.bi4vmr.tool.android.ability.framework.window.WindowManagerExtend
import net.bi4vmr.tool.android.ui.crosswindowblurtool.CrossWindowBlurHelper
import net.bi4vmr.tool.databinding.TestDialogBinding

/**
 * 测试悬浮窗。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class TestWindowKT(private val mContext: Context) : Dialog(mContext, R.style.Theme_FloatDialog) {

    companion object {
        private val TAG: String = TestWindowKT::class.java.simpleName
    }

    private val binding: TestDialogBinding = TestDialogBinding.inflate(LayoutInflater.from(mContext))

    init {
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        window?.run {
            decorView.setPadding(0, 0, 0, 0)
            attributes.apply {
                title = TAG
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
                type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG
                format = PixelFormat.RGBA_8888
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                // PRIVATE_FLAG_TRUSTED_OVERLAY(0x20000000)
                WindowManagerExtend.addPrivateFlags(this, WindowManagerExtend.PrivateFlags.TRUSTED_OVERLAY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        CrossWindowBlurHelper(binding.tvState, 5).init(binding.tvState)
        CrossWindowBlurHelper(binding.tvState2, 50).init(binding.tvState2)
    }
}
