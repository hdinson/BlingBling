package dinson.customview.fragment


/**
 * 自拍
 */
class _003GirlMeiZiTuSelfieFragment : _003GirlMeiZiTuNineGridFragment() {

    override fun getObservable() = mApi.meizituSelfiePic(mCurrentPage)

}
