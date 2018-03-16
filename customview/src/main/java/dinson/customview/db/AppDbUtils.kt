package dinson.customview.db

import android.database.sqlite.SQLiteDatabase
import dinson.customview.db.model.ZhihuTucao
import dinson.customview.download.model.DaoMaster
import dinson.customview.kotlin.getContext

/**
 * app数据库相关工具类
 */
object AppDbUtils {

    private val openHelper by lazy {
        DaoMaster.DevOpenHelper(getContext(), "AppDb", null)
    }

    /**
     * 获取可读数据库
     */
    private fun getReadableDatabase(): SQLiteDatabase = openHelper.readableDatabase

    /**
     * 获取可写数据库
     */
    private fun getWritableDatabase(): SQLiteDatabase = openHelper.writableDatabase


    /**
     * 插入单条知乎吐槽数据
     */

    fun insertZhihuTucao(zhihuTucao: ZhihuTucao) {
        DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.insertOrReplace(zhihuTucao)
    }

    /**
     * 插入多条知乎吐槽数据
     */
    fun insertMultZhihuTucao(zhihuTucaoList: List<ZhihuTucao>) {
        DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.insertOrReplaceInTx(zhihuTucaoList)
    }

    /**
     * 插入单条知乎吐槽数据
     */
    fun updateZhihuTucao(zhihuTucao: ZhihuTucao) {
        DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.update(zhihuTucao)
    }

    /**
     * 根据id查找知乎吐槽
     */
    fun queryById(key: Long): ZhihuTucao? {
        val builder = DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.queryBuilder()
        val list = builder.where(ZhihuTucaoDao.Properties.Id.eq(key)).list()
        return if (list.isEmpty()) null else list[0]
    }

    /**
     * 获取本地知乎吐槽数据
     */
    fun getLocalDatas(limit: Int = 20): List<ZhihuTucao> {
        val builder = DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.queryBuilder()
        return builder.orderDesc(ZhihuTucaoDao.Properties.Date).limit(limit).list()
    }

    /**
     * 获取某个时间以前的本地知乎吐槽数据
     */
    fun getLocalDatasBefore(time: Int, limit: Int = 20): List<ZhihuTucao> {
        val builder = DaoMaster(getWritableDatabase()).newSession().zhihuTucaoDao.queryBuilder()
        return builder.where(ZhihuTucaoDao.Properties.Date.lt(time)).orderDesc(ZhihuTucaoDao.Properties.Date).limit(limit).list()
    }
}
