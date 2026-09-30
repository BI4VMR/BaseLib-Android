package net.bi4vmr.tool.android.storage.room

import androidx.room.RoomDatabase

/**
 * SQL语句回调 - 合并占位符。
 *
 * 将原始SQL语句中的 `?` 占位符替换为实际参数，并输出日志。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
class MergeQueryLogger : RoomDatabase.QueryCallback {

    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        TODO("Not yet implemented")
    }
}
