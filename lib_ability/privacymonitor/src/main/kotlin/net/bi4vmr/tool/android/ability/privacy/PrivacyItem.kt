package net.bi4vmr.tool.android.ability.privacy

import android.graphics.drawable.Drawable
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity

/**
 * 实体类：隐私应用表项。
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
)
