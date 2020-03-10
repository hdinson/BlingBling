package dinson.customview.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dinson.customview.kotlin.loge

class DeletePng(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        compress()
        return Result.success()
    }

    private fun compress() {
        "压缩了图片".loge()
    }

}