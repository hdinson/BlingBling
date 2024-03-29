package dinson.customview.activity

import android.os.Bundle
import dinson.customview.R
import dinson.customview._global.BaseActivity
import com.dinson.blingbase.kotlin.click
import kotlinx.android.synthetic.main.activity__017_tetris.*

class _017TetrisActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__017_tetris)


        btnLeft.click { game.moveLeft() }
        btnRight.click { game.moveRight() }
        btnDownFast.click { game.fastMoveDown() }
        btnA.click { game.rotate() }
        btnReset.click { game.reSetGame() }
    }

    override fun onPause() {
        super.onPause()
        game.pauseGame()
    }

    override fun onResume() {
        super.onResume()
        game.startGame()
    }
}
