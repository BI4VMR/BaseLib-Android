package net.bi4vmr.tool.base.list

import android.graphics.drawable.Drawable
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity

/**
 * TODO 添加简述。
 *
 * TODO 添加详情。
 *
 * @author bi4vmr@outlook.com
 */
data class PrivacyVO(
    val title: String,
    val icon: Drawable?,
    val privacy: OpEntity
)
