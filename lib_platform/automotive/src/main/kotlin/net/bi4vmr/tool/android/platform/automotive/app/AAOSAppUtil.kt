package net.bi4vmr.tool.android.platform.automotive.app

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresPermission
import org.xmlpull.v1.XmlPullParser
import java.util.ArrayDeque

/**
 * Automotive应用程序相关工具。
 *
 * [Automotive应用配置官方指南](https://developer.android.com/training/cars/media/automotive-os?hl=zh-cn#manifest-car-app)
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object AAOSAppUtil {

    var debug = false

    private const val TAG = "AAOSAppUtil"

    private const val METADATA_KEY_APP_TYPE = "com.android.automotive"

    private const val TAG_AUTOMOTIVE_APP = "automotiveApp"

    private const val TAG_USES = "uses"

    private const val ATTRIBUTE_NAME = "name"

    /**
     * 判断应用程序是否为AAOS媒体应用。
     *
     * @param[context] 上下文。
     * @param[packageName] 包名。
     * @return 应用程序是否为AAOS媒体应用。
     */
    @JvmStatic
    fun isMediaApp(context: Context, packageName: String): Boolean {
        return getTypes(context, packageName).contains(AAOSAppType.MEDIA)
    }

    /**
     * 判断应用程序是否为AAOS视频应用。
     *
     * @param[context] 上下文。
     * @param[packageName] 包名。
     * @return 应用程序是否为AAOS视频应用。
     */
    @JvmStatic
    fun isVideoApp(context: Context, packageName: String): Boolean {
        return getTypes(context, packageName).contains(AAOSAppType.VIDEO)
    }

    /**
     * 判断应用程序是否配置了AAOS应用类型。
     *
     * 若应用程序配置了任意类型，则返回 `true` 。
     *
     * @param[context] 上下文。
     * @param[packageName] 包名。
     * @return 应用程序是否配置了AAOS应用类型。
     */
    @JvmStatic
    fun isAutomotiveApp(context: Context, packageName: String): Boolean {
        return getTypesRaw(context, packageName).isNotEmpty()
    }

    /**
     * 获取所有配置了AAOS应用类型的应用信息。
     *
     * 若要使用该方法，请确保已在 `AndroidManifest.xml` 中声明了 `QUERY_ALL_PACKAGES` 权限。
     *
     * 该方法耗时取决于应用数量，建议在子线程中调用。
     *
     * @param[context] 上下文。
     * @return [ApplicationInfo]列表。
     */
    @RequiresPermission(Manifest.permission.QUERY_ALL_PACKAGES)
    @JvmStatic
    fun getAutomotiveApps(context: Context): List<ApplicationInfo> {
        val tsStart = SystemClock.uptimeMillis()

        val packageManager = context.packageManager
        val appInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(0L)
            )
        } else {
            // 已经实现了兼容适配，忽略警告。
            @Suppress("DEPRECATION")
            packageManager.getInstalledApplications(0)
        }

        return appInfos.filter { info ->
            isAutomotiveApp(context, info.packageName)
        }.also {
            if (debug) {
                val tsEnd = SystemClock.uptimeMillis()
                Log.d(TAG, "GetAutomotiveApps cost ${tsStart - tsEnd} ms, detail start:")
                it.forEach { app ->
                    Log.d(TAG, "Find AAOS App:[${app.packageName}] Types:${getTypesRaw(context, app.packageName)}")
                }
                Log.d(TAG, "GetAutomotiveApps detail end.")
            }
        }
    }

    /**
     * 解析AAOS应用类型。
     *
     * 返回本工具的枚举列表，如果字符串未在枚举中定义，将被过滤。
     *
     * @param[context] 上下文。
     * @param[packageName] 包名。
     * @return 应用类型枚举列表。
     */
    @JvmStatic
    fun getTypes(context: Context, packageName: String): List<AAOSAppType> {
        return getTypesRaw(context, packageName)
            .mapNotNull {
                // 跳过枚举类中未匹配到的项
                val type = AAOSAppType.fromText(it)
                if (type == null) {
                    Log.w(TAG, "Unknown app type [$it] in app [$packageName] config, ignored!")
                    return@mapNotNull null
                }

                type
            }
    }

    /**
     * 解析AAOS应用类型。
     *
     * 返回原始字符串列表。
     *
     * @param[context] 上下文。
     * @param[packageName] 包名。
     * @return 应用类型字符串列表。
     */
    @JvmStatic
    fun getTypesRaw(context: Context, packageName: String): List<String> {
        try {
            val packageManager = context.packageManager
            val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                // 已经实现了兼容适配，忽略警告。
                @Suppress("DEPRECATION")
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            }

            // 如果应用未声明Automotive应用类型，则返回空列表。
            val configResID = appInfo.metaData?.getInt(METADATA_KEY_APP_TYPE) ?: 0
            if (configResID == 0) {
                return emptyList()
            }

            // 解析配置文件中的类型标签
            val appRes = packageManager.getResourcesForApplication(appInfo)
            return appRes.getXml(configResID).use { parser ->
                parseAutomotiveAppTypes(parser)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.w(TAG, "App [$packageName] was not found!")
            return emptyList()
        } catch (e: Resources.NotFoundException) {
            Log.w(TAG, "Read app [$packageName] automotive config failed!", e)
            return emptyList()
        }
    }

    // 从XML配置文件解析应用类型列表
    @JvmStatic
    private fun parseAutomotiveAppTypes(parser: XmlPullParser): List<String> {
        try {
            val appTypes = mutableListOf<String>()
            val tagStack = ArrayDeque<String>()
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    val tag = parser.name
                    tagStack.addFirst(tag)
                    // 检查配置文件是否符合预期的结构
                    if (!validTagStack(tagStack)) {
                        Log.w(TAG, "Config file format error! Tags:$tagStack")
                        return emptyList()
                    }

                    if (tag == TAG_USES) {
                        val nameValue = parser.getAttributeValue(null, ATTRIBUTE_NAME)
                        if (TextUtils.isEmpty(nameValue)) {
                            Log.w(TAG, "Read a empty tag, ignored!")
                            continue
                        }

                        appTypes.add(nameValue)
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    tagStack.removeFirst()
                }

                eventType = parser.next()
            }
            return appTypes
        } catch (e: Exception) {
            Log.w(TAG, "Parsing XML file error! Reason:[${e.message}]", e)
            return emptyList()
        }
    }

    // 检查当前标签是否按照预期层级配置
    private fun validTagStack(tagStack: ArrayDeque<String>): Boolean {
        return when (tagStack.size) {
            1 -> TAG_AUTOMOTIVE_APP == tagStack.peekFirst()
            2 -> TAG_USES == tagStack.peekFirst()
            else -> false
        }
    }
}
