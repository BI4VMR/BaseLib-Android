package net.bi4vmr.tool.base

import android.app.Dialog
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import net.bi4vmr.tool.R
import net.bi4vmr.tool.base.list.ListAdapterKT
import net.bi4vmr.tool.base.list.PrivacyVO
import net.bi4vmr.tool.databinding.PrivacyListBinding

/**
 * TODO 添加简述。
 *
 * TODO 添加详情。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class PrivacyListWindowKT(private val mContext: Context) : Dialog(mContext, R.style.Theme_VPADialog) {

    companion object {
        private val TAG: String = PrivacyListWindowKT::class.java.simpleName
    }

    private val binding: PrivacyListBinding = PrivacyListBinding.inflate(LayoutInflater.from(mContext))
    private val adapter: ListAdapterKT by lazy { ListAdapterKT(mContext) }

    init {
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        window?.run {
            setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
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
                WindowManagerUtil.addPrivateFlags(this, WindowManagerUtil.PrivateFlags.TRUSTED_OVERLAY)
            }
        }

        binding.list.adapter = adapter
    }

    fun updateData(data: List<PrivacyVO>) {
        Handler(Looper.getMainLooper()).post {
            adapter.updateData(data)
        }
    }
}
