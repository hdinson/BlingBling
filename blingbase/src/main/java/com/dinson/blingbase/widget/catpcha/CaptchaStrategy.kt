package com.dinson.blingbase.widget.catpcha

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

abstract class CaptchaStrategy() {

    /**
     * 定义缺块的形状
     *
     * @param blockSize 单位dp，注意转化为px
     * @return path of the shape
     */
    abstract fun getBlockShape(blockSize: Int): Path?

    /**
     * 定义缺块的位置信息
     *
     * @param width  picture width
     * @param height picture height
     * @return position info of the block
     */
    abstract fun getBlockPositionInfo(width: Int, height: Int, blockSize: Int): PositionInfo?

    /**
     * 定义滑块图片的位置信息(只有设置为无滑动条模式有用)
     *
     * @param width  picture width
     * @param height picture height
     * @return position info of the block
     */
    open fun getPositionInfoForSwipeBlock(width: Int, height: Int, blockSize: Int): PositionInfo? {
        return getBlockPositionInfo(width, height, blockSize)
    }

    /**
     * 获得缺块阴影的Paint
     */
    abstract fun blockShadowPaint(): Paint

    /**
     * 获得滑块图片的Paint
     */
    abstract fun blockBitmapPaint(): Paint

    /**
     * 装饰滑块图片，在绘制图片后执行，即绘制滑块前景
     */
    open fun decorationSwipeBlockBitmap(canvas: Canvas, shape: Path) {
    }
}