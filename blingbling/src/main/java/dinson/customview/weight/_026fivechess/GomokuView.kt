package dinson.customview.weight._026fivechess

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import dinson.customview.R
import dinson.customview.kotlin.logi
import dinson.customview.kotlin.toast

class GomokuView @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private var mGomokuViewSettings: GomokuViewSettings
    private val mChessArray: Array<IntArray>
    private val mChessList: IntArray
    private val mChessArrayList = ArrayList<Int>()
    private val mChessListFar: IntArray
    private var mStepNum = 0
    private var mStepFarNum = 0
    private val mWhiteChess by lazy { BitmapFactory.decodeResource(context.resources, R.drawable._026_white_chess) }
    private val mBlackChess by lazy { BitmapFactory.decodeResource(context.resources, R.drawable._026_black_chess) }
    private val mRect = Rect()
    private var mIsGameOver = false
    private var mIsThinking = false
    private var mPreWidth = 0  //小格子宽度
    private var mPadding = 0f  //棋盘距离控件宽度
    private var mSmallDx = 0f  //棋盘小间距
    private var mTurnWho = 1  //棋盘小间距
    private var mGameCallBack: GameCallBack? = null


    //todo
    var DO_DOSTONE = 3

    init {
        mPaint.isAntiAlias = true
        mGomokuViewSettings = GomokuViewSettings(context, attrs)

        mPaint.color = Color.BLACK
        val size = mGomokuViewSettings.mGomokuSize
        mChessArray = Array(size) { IntArray(size) }
        mChessList = IntArray(size * size + 2)
        mChessListFar = IntArray(size * size + 2)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val min = Math.min(w, h)
        this.setMeasuredDimension(min, min)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = mGomokuViewSettings.mGomokuSize
        mPreWidth = width / size
        mPadding = (width - mPreWidth * (size - 1)) / 2f
        mSmallDx = mPadding / 5.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = mGomokuViewSettings.mGomokuSize
        mPaint.textSize = mPreWidth / 3.0f
        mPaint.color = Color.BLACK


        //画棋盘
        for (i in 0 until size) {
            val offset = i * mPreWidth + mPadding
            canvas.drawLine(mPadding, offset, width - mPadding, offset, mPaint)
            canvas.drawLine(offset, mPadding, offset, width - mPadding, mPaint)
            canvas.drawText(convertX(i), offset, width - mPadding / 4.0f, mPaint)
            canvas.drawText(convertY(i), mPadding / 4.0f, offset, mPaint)
        }
        mPaint.style = Paint.Style.STROKE //空心矩形框
        canvas.drawRect(mPadding - mSmallDx, mPadding - mSmallDx, width - mPadding + mSmallDx, width - mPadding + mSmallDx, mPaint)
        mPaint.style = Paint.Style.FILL //实心矩形框

        val middle = size / 2
        val quarter = middle / 2
        if (size % 2 != 0) { //单数画中间点
            canvas.drawRect(middle * mPreWidth + mPadding - mSmallDx, middle * mPreWidth + mPadding - mSmallDx, middle * mPreWidth + mPadding + mSmallDx, middle * mPreWidth + mPadding + mSmallDx, mPaint)
        }

        //画4点
        canvas.drawRect(quarter * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect((size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect(quarter * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect((size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, mPaint)

        //画棋子  5
        mChessArrayList.forEachIndexed { step, i ->
            val x = i / size
            val y = i % size
            val dx = mPadding + x * mPreWidth
            val dy = mPadding + y * mPreWidth
            mRect.set((dx - mPadding).toInt(), (dy - mPadding).toInt(), (dx + mPadding).toInt(), (dy + mPadding).toInt())

            when (mChessArray[x][y]) {
                2 -> {
                    canvas.drawBitmap(mWhiteChess, null, mRect, mPaint)
                    drawChessText(canvas, step, x, y, Color.BLACK)
                }
                1 -> {
                    canvas.drawBitmap(mBlackChess, null, mRect, mPaint)
                    drawChessText(canvas, step, x, y, Color.WHITE)
                }
            }
        }
    }

    private var mDx = 0
    private var mDy = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val downX = event.x
                val downY = event.y
                mDx = (downX / mPreWidth).toInt()
                mDy = (downY / mPreWidth).toInt()
                if (downX < mPadding / 2.0f ||
                    downX > width - mPadding / 2.0f ||
                    downY < mPadding / 2.0f ||
                    downY > width - mPadding / 2.0f ||
                    mChessArray[mDx][mDy] == 2 ||
                    mChessArray[mDx][mDy] == 1) {

                    "ACTION_DOWN:$mDx  $mDy".logi()
                    mDx = -1
                    mDy = -1
                    return false
                }
                //  false
            }
            MotionEvent.ACTION_UP -> {
                if (mIsGameOver) {
                    "游戏已经结束，请重新开始！".toast()
                    "------  IsGameOver  ------".logi()
                    return false
                }
                if (mIsThinking) {
                    "------  IsThinking  ------".logi()
                    return false
                }

                "------  落子  ------".logi()
                val upX = event.x
                val upY = event.y
                if (mDx < 0 || mDy < 0) {
                    // return false
                }
                val ux = (upX / mPreWidth).toInt()
                val uy = (upY / mPreWidth).toInt()

                //if (mChessArray[mDx][mDy] == 2 || mChessArray[mDx][mDy] == 1) {
                // return false
                //}
                val turnWho = mTurnWho
                if (Math.abs(ux - mDx) <= 1 && Math.abs(uy - mDy) <= 1) {
                    doStone(mTurnWho, mDx, mDy, "Player")
                } else {
                    doStone(mTurnWho, mDx, mDy, "Player--滑动落子")
                }
                mGameCallBack?.aiTEFACES(DO_DOSTONE, oppTurn(mTurnWho), mDx, mDy, mStepNum)

                val checkGameOver = this.checkGameOver()
                mGameCallBack?.onGameOver(checkGameOver)
                if (!mIsGameOver) {
                    if (Math.abs(ux - mDx) <= 1 && Math.abs(uy - mDy) <= 1 && mGameCallBack != null) {
                        mDx = -1
                        mDy = -1
                        mGameCallBack!!.FaceMode(true)
                        mGameCallBack!!.aiRun(mTurnWho)
                    }
                } else {
                    if (checkGameOver == GomokuResult.BLACK_WIN || checkGameOver == GomokuResult.BLACK_WIN) {
                        if (turnWho == 2) {
                            "玩家执白棋赢!".logi()
                        }
                        if (turnWho == 1) {
                            "玩家执黑棋赢!".logi()
                        }
                        printChessList()
                    }
                }
                // false
            }
        }
        return true
    }

    private fun drawChessText(canvas: Canvas, step: Int, n2: Int, n3: Int, color: Int) {
        val number = String.format("%d", step + 1)
        val x = n2 * mPreWidth + mPadding
        val y = n3 * mPreWidth + mPadding
        if (step + 1 == mStepNum) {
            mPaint.setARGB(255, 190, 190, 0)
            canvas.drawRect(x - mSmallDx * 3.0f, y - mSmallDx * 2.0f, x + mSmallDx * 3.0f, y + mSmallDx * 2.0f, mPaint)
        }
        mPaint.color = color
        when {
            step < 9 -> canvas.drawText(number, x - mSmallDx, y + mSmallDx, mPaint)
            step < 99 -> canvas.drawText(number, x - mSmallDx * 2.0f, y + mSmallDx, mPaint)
            else -> canvas.drawText(number, x - mSmallDx * 3.0f, y + mSmallDx, mPaint)
        }
    }

    /**
     * 打印棋谱
     */
    private fun printChessList() {
        val size = mGomokuViewSettings.mGomokuSize
        val str = mChessArrayList.joinToString(", ") { "${convertX(it / size)}${convertY(it % size)}" }
        "棋谱输出: $str".logi()
    }

    /**
     * 检查游戏是否结束
     */
    private fun checkGameOver(): GomokuResult {
        var i = 0
        var n = 1
        val size = mGomokuViewSettings.mGomokuSize

        while (i < size) {
            var j = 0
            while (j < size) {
                var n2 = n
                if (mChessArray[i][j] != 1) {
                    n2 = n
                    if (mChessArray[i][j] != 2) {
                        n2 = 0
                    }
                }
                if ((mChessArray[i][j] == 2 || mChessArray[i][j] == 1) && isFiveSame(size, i, j)) {
                    val n3 = if (mChessArray[i][j] == 2) {
                        GomokuResult.WHITE_WIN
                    } else {
                        GomokuResult.BLACK_WIN
                    }
                    mIsGameOver = true
                    return n3
                } else {
                    ++j
                    n = n2
                }
            }
            ++i
        }
        if (n != 0) {
            mIsGameOver = true
            return GomokuResult.DREW
        }
        return GomokuResult.UNKNOWN
    }

    /**
     * 是否五子连珠
     */
    private fun isFiveSame(size: Int, x: Int, y: Int): Boolean {
        val n3 = x + 4
        if (n3 < size && mChessArray[x][y] == mChessArray[x + 1][y] &&
            mChessArray[x][y] == mChessArray[x + 2][y] &&
            mChessArray[x][y] == mChessArray[x + 3][y] &&
            mChessArray[x][y] == mChessArray[n3][y]) {
            return true
        }
        val n4 = y + 4
        if (n4 < size && mChessArray[x][y] == mChessArray[x][y + 1] &&
            mChessArray[x][y] == mChessArray[x][y + 2] &&
            mChessArray[x][y] == mChessArray[x][y + 3] &&
            mChessArray[x][y] == mChessArray[x][n4]) {
            return true
        }
        if (n4 < size && n3 < size &&
            mChessArray[x][y] == mChessArray[x + 1][y + 1] &&
            mChessArray[x][y] == mChessArray[x + 2][y + 2] &&
            mChessArray[x][y] == mChessArray[x + 3][y + 3] &&
            mChessArray[x][y] == mChessArray[n3][n4]) {
            return true
        }
        val n5 = y - 4
        return n5 >= 0 && n3 < size &&
            mChessArray[x][y] == mChessArray[x + 1][y - 1] &&
            mChessArray[x][y] == mChessArray[x + 2][y - 2] &&
            mChessArray[x][y] == mChessArray[x + 3][y - 3] &&
            mChessArray[x][y] == mChessArray[n3][n5]
    }


    private fun doStone(n: Int, x: Int, y: Int, format: String) {
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            "DoStone $x $y out of range".logi()
            return
        }
        if (mChessArray[x][y] != 0) {
            "DoStone $x $y already set".logi()
            return
        }
        val s = if (n == 1) "黑" else "白"
        "$s${mStepNum + 1} @ ${convertX(x)}${convertY(y)} : $format".logi()

        val chessNum = mGomokuViewSettings.mGomokuSize * x + y
        mChessList[mStepNum] = chessNum
        mChessArrayList.add(chessNum)

        if (mChessList[mStepNum] != mChessListFar[mStepNum]) {
            mChessListFar[mStepNum] = mChessList[mStepNum]
            mStepFarNum = mStepNum
            ++mStepFarNum
        }
        ++mStepNum
        mChessArray[x][y] = n
        mTurnWho = oppTurn(n)
        postInvalidate()
    }

    private fun oppTurn(n: Int) = if (n == 1) 2 else 1
    private fun convertX(x: Int) = (x + 65).toChar().toString()
    private fun convertY(y: Int) = if (mGomokuViewSettings.mGomokuSize - y < 10) (mGomokuViewSettings.mGomokuSize - y).toString()
    else (87 + mGomokuViewSettings.mGomokuSize - y).toChar().toString()

}