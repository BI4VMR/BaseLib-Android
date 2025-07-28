package net.bi4vmr.tool.android.storage.room

import androidx.room.RoomDatabase

/**
 * SQL语句回调 - 简单日志打印。
 *
 * 向Logcat输出SQL语句与参数，二者以 `|` 分隔。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class SimpleQueryLogger : RoomDatabase.QueryCallback {

    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        TODO("Not yet implemented")
    }
}
