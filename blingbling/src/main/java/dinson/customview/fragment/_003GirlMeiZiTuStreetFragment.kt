package dinson.customview.fragment


/**
 * 街拍
 */
class _003GirlMeiZiTuStreetFragment : _003GirlMeiZiTuNineGridFragment() {

    override fun getObservable() = mApi.meizituStreetPic(mCurrentPage)

}
