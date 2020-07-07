package dinson.customview.db

import android.database.sqlite.SQLiteDatabase
import com.dinson.blingbase.RxBling
import dinson.customview.db.model.DaoMaster
import dinson.customview.db.model.SearchHistory027
import dinson.customview.db.model.SearchHistory027Dao

/**
 * app数据库相关工具类
 */
object SearchHistory027DbUtils {

    private val openHelper by lazy {
        DaoMaster.DevOpenHelper(RxBling.context, "History027", null)
    }

    /**
     * 获取可读数据库
     */
    public fun getReadableDatabase(): SQLiteDatabase = openHelper.readableDatabase

    /**
     * 获取可写数据库
     */
    private fun getWritableDatabase(): SQLiteDatabase = openHelper.writableDatabase


    /**
     * 插入单条知乎吐槽数据
     */

    fun insertHistory(history: SearchHistory027) {
        DaoMaster(getWritableDatabase()).newSession().searchHistory027Dao.insertOrReplace(history)
    }

    /**
     * 插入多条知乎吐槽数据
     */
    fun insertMultiHistory(historyList: List<SearchHistory027>) {
        DaoMaster(getWritableDatabase()).newSession().searchHistory027Dao.insertOrReplaceInTx(historyList)
    }

    /**
     * 插入单条知乎吐槽数据
     */
    fun updateHistory(history: SearchHistory027) {
        DaoMaster(getWritableDatabase()).newSession().searchHistory027Dao.update(history)
    }

    /**
     * 分页查询
     */
    fun getHistory(page: Int): List<SearchHistory027> {
        return DaoMaster(getReadableDatabase()).newSession().searchHistory027Dao
            .queryBuilder().offset(page * 20).limit(20)
            .orderDesc(SearchHistory027Dao.Properties.Id).list()
    }

}
