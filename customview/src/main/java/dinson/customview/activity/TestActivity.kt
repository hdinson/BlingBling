package dinson.customview.activity

import android.os.Bundle
import android.widget.Toast
import dinson.customview.R
import dinson.customview._global.BaseActivity


class TestActivity : BaseActivity() {

    lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)



    }



}