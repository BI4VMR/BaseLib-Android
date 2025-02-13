package net.bi4vmr.tool.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import net.bi4vmr.tool.R
import net.bi4vmr.tool.android.ability.framework.window.WindowManagerExtend
import net.bi4vmr.tool.android.ability.privacymonitor.PrivacyItem
import net.bi4vmr.tool.databinding.PrivacyListBinding

/**
 * 权限列表悬浮窗。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class PrivacyListWindowKT(private val mContext: Context) : Dialog(mContext, R.style.Theme_FloatDialog) {

    companion object {
        private val TAG: String = PrivacyListWindowKT::class.java.simpleName
    }

    private val binding: PrivacyListBinding = PrivacyListBinding.inflate(LayoutInflater.from(mContext))
    private val adapter: PrivacyListAdapterKT by lazy { PrivacyListAdapterKT() }

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

        binding.list.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    fun updateState(state: Boolean) {
        if (state) {
            binding.tvState.text = "存在正在使用敏感权限的应用"
            binding.tvState.setBackgroundColor(Color.RED)
        } else {
            binding.tvState.text = "没有正在使用敏感权限的应用"
            binding.tvState.setBackgroundColor(Color.GREEN)
        }
    }

    fun updateData(data: List<PrivacyItem>) {
        Handler(Looper.getMainLooper()).post {
            adapter.updateData(data)
        }
    }

    fun clearData() {
        Handler(Looper.getMainLooper()).post {
            adapter.clearData()
        }
    }
}
