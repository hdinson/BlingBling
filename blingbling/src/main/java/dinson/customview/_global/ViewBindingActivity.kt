package dinson.customview._global

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.dinson.blingbase.annotate.BindEventBus
import com.dinson.blingbase.utils.SystemBarModeUtils
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dinson.customview.BuildConfig
import dinson.customview.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType


/**
 * 所有activity的基类
 */
@SuppressLint("Registered")
open class ViewBindingActivity<T : ViewBinding> : BaseActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as T
        setContentView(binding.root)
    }
}
