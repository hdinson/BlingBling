package dinson.customview.fragment


/**
 * 精品
 *
 */
class _003GirlMeiZiTuHotFragment : _003GirlMeiZiTuPicSetFragment() {


    override fun getObservable() = mApi.meizituHotPic(mCurrentPage)

    override fun needLoadMore() = false
}
