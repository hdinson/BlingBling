package dinson.customview.weight._016parallaximgview

/**
 *   视差滑动监听
 */
interface OnParallaxScrollListener {

    /**
     * Call when the image is scrolling
     *
     * @param view           the panoramaImageView shows the image
     * @param offsetProgress value between (-1, 1) indicating the offset progress.
     * -1 means the image scrolls to show its left(top) bound,
     * 1 means the image scrolls to show its right(bottom) bound.
     */
    fun onScrolled(view: ParallaxImageView, offsetProgress: Float)
}