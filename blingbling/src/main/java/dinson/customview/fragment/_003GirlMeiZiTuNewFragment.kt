package dinson.customview.fragment


/**
 * 最新
 *
 */
class _003GirlMeiZiTuNewFragment : _003GirlMeiZiTuPicSetFragment() {


    override fun getObservable() = mApi.meizituNewPic(mCurrentPage)


}
