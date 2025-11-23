package net.bi4vmr.gradle.util

/**
 * 日志相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object LogUtil {

    fun info(message: String) {
        println("PrivateRepoPlugin-$message")
    }

    fun error(message: String) {
        System.err.println("PrivateRepoPlugin-$message")
    }
}
