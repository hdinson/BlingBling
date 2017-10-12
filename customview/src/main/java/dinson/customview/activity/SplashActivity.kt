package dinson.customview.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dinson.customview.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
