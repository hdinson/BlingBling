package dinson.customview.activity

import android.os.Bundle
import android.text.Selection.moveDown
import android.widget.Toast
import com.loc.y
import dinson.customview.R
import dinson.customview._global.BaseActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class TestActivity : BaseActivity() {

    lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)



    }



}