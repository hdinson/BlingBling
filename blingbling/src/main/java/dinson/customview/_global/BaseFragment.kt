package dinson.customview._global


import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * fragment基类
 */
abstract class BaseFragment : Fragment() {


    private val mCompositeDisposable = CompositeDisposable()
    private var mLazyLoaded = false
    fun Disposable.addToManaged() {
        mCompositeDisposable.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        if (!mLazyLoaded && onResumeLazyLoad()) {
            mLazyLoaded = true
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    open fun onResumeLazyLoad(): Boolean {
        return false
    }

    /**
     * 设置懒加载完成
     */
    public fun setLazyLoaded(isLazyLoaded: Boolean) {
        mLazyLoaded = isLazyLoaded
    }
}
