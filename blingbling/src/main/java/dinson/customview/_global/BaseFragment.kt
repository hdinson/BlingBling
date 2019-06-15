package dinson.customview._global

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * fragment基类
 */
abstract class BaseFragment : Fragment(){


    private val mCompositeDisposable = CompositeDisposable()
    fun Disposable.addToManaged(){
        mCompositeDisposable.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}
