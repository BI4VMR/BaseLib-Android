package net.bi4vmr.tool.android.ability.privacymonitor

import android.graphics.drawable.Drawable
import net.bi4vmr.tool.android.ability.framework.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacymonitor.util.AppInfoHelper

/**
 * 实体类：隐私权限应用。
 *
 * 对[OpEntity]的封装，包括名称与图标，UI层可以直接作为VO使用。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
data class PrivacyItem(

    /**
     * 应用名称。
     */
    val title: String,

    /**
     * 应用图标。
     */
    val icon: Drawable,

    /**
     * 原始OP信息。
     */
    val op: OpEntity
) {

    companion object {

        /**
         * 将[OpEntity]转换为[PrivacyItem]。
         *
         * @param[op] [OpEntity]对象。
         * @param[defaultIcon] 默认应用图标。
         * @return [PrivacyItem]对象。
         */
        internal fun parseFromOpEntity(op: OpEntity, defaultIcon: Drawable): PrivacyItem {
            // 获取应用名称，如果获取失败则填写为包名。
            val appName: String = AppInfoHelper.getLabel(op.packageName) ?: op.packageName
            // 获取应用图标，如果获取失败则填写为默认图片。
            val icon: Drawable = AppInfoHelper.getIcon(op.packageName) ?: defaultIcon

            return PrivacyItem(appName, icon, op)
        }
    }
}
