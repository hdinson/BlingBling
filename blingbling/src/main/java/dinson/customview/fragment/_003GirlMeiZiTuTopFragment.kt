package dinson.customview.fragment


/**
 * 最热
 */
class _003GirlMeiZiTuTopFragment : _003GirlMeiZiTuPicSetFragment() {

    override fun getObservable() = mApi.meizituTopPic(mCurrentPage)

    override fun needIncrement() = false

}
