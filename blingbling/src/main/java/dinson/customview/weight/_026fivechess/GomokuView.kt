package dinson.customview.weight._026fivechess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bf92.ai.AI
import dinson.customview.R
import com.dinson.blingbase.kotlin.loge
import com.dinson.blingbase.kotlin.logi


class GomokuView @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr), AICallBack {


    private val mGomokuSize = 15   //目前只支持15路五子棋
    private val mPaint = Paint()
    private var mGomokuViewSettings: GomokuViewSettings

    //二位数组，虚拟棋盘，0表示无棋，1表示黑棋，2表示白旗
    private val mChessArray: Array<IntArray> = Array(mGomokuSize) { IntArray(mGomokuSize) }
    //存储棋步，一位数组，值用15进制表示
    private val mChessList: IntArray = IntArray(mGomokuSize * mGomokuSize)
    //存储棋步，用于恢复，一位数组，值用15进制表示
    private val mChessListFar: IntArray = IntArray(mGomokuSize * mGomokuSize)
    //有顺序的棋谱
    private val mChessArrayList = ArrayList<Int>()

    private var mStepNum = 0
    private var mStepFarNum = 0

    private val mWhiteChess by lazy { BitmapFactory.decodeResource(context.resources, R.drawable._026_white_chess) }
    private val mBlackChess by lazy { BitmapFactory.decodeResource(context.resources, R.drawable._026_black_chess) }
    private val mRect = Rect()
    private var mIsGameOver = false
    private var mIsThinking = false
    private var mThinkPoint = 0     //ai思考的点位，15进制数
    private var mPreWidth = 0       //小格子宽度
    private var mPadding = 0f       //棋盘距离控件宽度
    private var mSmallDx = 0f       //棋盘小间距
    private var mTurnWho = 1        //1黑，2白
    private var mGameCallBack: GameCallBack? = null
    private var mStepTime = 5       //单步思考时间

    private var mAI: AI? = null
    private var mGameMode = true    //游戏模式,true:人机模式 ,false:人人模式
    private var mGameState: GomokuGameState = GomokuGameState.GOING


    init {
        mPaint.isAntiAlias = true
        mGomokuViewSettings = GomokuViewSettings(context, attrs)

        mPaint.color = mGomokuViewSettings.mGomokuLineColor
        viewTreeObserver.addOnGlobalLayoutListener {
            if (mAI == null) {
                "init AI".logi()
                mAI = AI(mChessList, this)
                aiRun()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val min = Math.min(w, h)
        this.setMeasuredDimension(min, min)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = mGomokuSize

        mPreWidth = width / size
        mPadding = (width - mPreWidth * (size - 1)) / 2f
        mSmallDx = mPadding / 5.0f

        mPaint.textSize = mPreWidth / 3.0f
        mPaint.color = mGomokuViewSettings.mGomokuLineColor
        //构建背景
        val mGameBoardBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mGameBoardBitmap)
        if (background != null) {
            if (background is ColorDrawable) {
                val back = background as ColorDrawable
                canvas.drawColor(back.color)
            } else {
                val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
                val back = background as BitmapDrawable
                //canvas.drawBitmap(back.bitmap, 0f, 0f, mPaint)
                canvas.drawBitmap(back.bitmap, null, rectF, mPaint)
            }
        }

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

        //画5点
        //方形点
        /*canvas.drawRect(middle * mPreWidth + mPadding - mSmallDx, middle * mPreWidth + mPadding - mSmallDx, middle * mPreWidth + mPadding + mSmallDx, middle * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect(quarter * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect((size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect(quarter * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, quarter * mPreWidth + mPadding + mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, mPaint)
        canvas.drawRect((size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding - mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, (size - 1 - quarter) * mPreWidth + mPadding + mSmallDx, mPaint)
        */
        //圆形点
        canvas.drawCircle(middle * mPreWidth + mPadding, middle * mPreWidth + mPadding, mSmallDx, mPaint)
        canvas.drawCircle(quarter * mPreWidth + mPadding, quarter * mPreWidth + mPadding, mSmallDx, mPaint)
        canvas.drawCircle((size - 1 - quarter) * mPreWidth + mPadding, quarter * mPreWidth + mPadding, mSmallDx, mPaint)
        canvas.drawCircle(quarter * mPreWidth + mPadding, (size - 1 - quarter) * mPreWidth + mPadding, mSmallDx, mPaint)
        canvas.drawCircle((size - 1 - quarter) * mPreWidth + mPadding, (size - 1 - quarter) * mPreWidth + mPadding, mSmallDx, mPaint)



        background = BitmapDrawable(resources, mGameBoardBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = mGomokuSize

        //画棋子  5
        mChessArrayList.forEachIndexed { step, i ->
            val x = i / size
            val y = i % size
            val dx = mPadding + x * mPreWidth
            val dy = mPadding + y * mPreWidth
            mRect.set((dx - mPreWidth / 2).toInt(), (dy - mPreWidth / 2).toInt(), (dx + mPreWidth / 2).toInt(), (dy + mPreWidth / 2).toInt())

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

        if (mIsThinking && mThinkPoint > 0) {

            val pointX = mThinkPoint / 15
            val pointY = mThinkPoint % 15

            mPaint.color = Color.RED

            val n8 = pointY.toFloat()
            canvas.drawRect(mPreWidth * pointX + mPadding - mSmallDx * 1.0f,
                mPreWidth * n8 + mPadding - mSmallDx * 1.0f,
                pointX * mPreWidth + mPadding + mSmallDx * 1.0f,
                n8 * mPreWidth + mPadding + mSmallDx * 1.0f,
                mPaint)
        }
    }

    private var mDx = 0
    private var mDy = 0


    @SuppressLint("ClickableViewAccessibility")
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
                    mGameCallBack?.onGameOver(mGameState)
                    "------  GameOver  ------".logi()
                    return false
                }
                if (mIsThinking) {
                    "------  IsThinking  ------".logi()
                    return false
                }

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
                if (Math.abs(ux - mDx) <= 1 && Math.abs(uy - mDy) <= 1) {
                    doStone(mTurnWho, mDx, mDy, "Player")
                } else {
                    doStone(mTurnWho, mDx, mDy, "Player--滑动落子")
                }
                //通知ai算法 
                mAI?.aiTEFACES(AI.DO_DOSTONE, oppTurn(mTurnWho), mDx, mDy, mStepNum)
                mGameCallBack?.onChessChange()

                //判断是否结束游戏
                //checkGameOver()
                checkGameOver(mGomokuSize, mDx, mDy)
                if (!mIsGameOver) {
                    //判断游戏模式
                    if (null != mAI && Math.abs(ux - mDx) <= 1 && Math.abs(uy - mDy) <= 1) {
                        mDx = -1
                        mDy = -1
                        aiRun()
                    }
                }
            }
        }
        return true
    }

    private fun drawChessText(canvas: Canvas, step: Int, n2: Int, n3: Int, color: Int) {
        val number = String.format("%d", step + 1)
        val x = n2 * mPreWidth + mPadding
        val y = n3 * mPreWidth + mPadding
        if (step + 1 == mStepNum) {
            mPaint.setARGB(255, 102, 153, 0)
            canvas.drawRect(x - mSmallDx * 2.0f, y - mSmallDx * 2.0f,
                x + mSmallDx * 2.0f, y + mSmallDx * 2.0f, mPaint)
        }
        mPaint.color = color
        when {
            step < 9 -> canvas.drawText(number, x - mSmallDx, y + mSmallDx, mPaint)
            step < 99 -> canvas.drawText(number, x - mSmallDx * 2.0f, y + mSmallDx, mPaint)
            else -> canvas.drawText(number, x - mSmallDx * 3.0f, y + mSmallDx, mPaint)
        }
    }


    /**
     * ai计算结束输出结果
     */
    override fun aiCompleted(x: Int, y: Int) {
        Handler(Looper.getMainLooper()).post {
            mGameCallBack?.onAiRunState(false)
        }
        mIsThinking = false
        mThinkPoint = 0
        if (x >= 0 && y >= 0 && x < mGomokuSize && y < mGomokuSize) {
            doStone(mTurnWho, x, y, "ai")
            //checkGameOver()
            checkGameOver(mGomokuSize, x, y)
            Handler(Looper.getMainLooper()).post {
                mGameCallBack?.onChessChange()
            }
        } else {
            "Game Error. Check the game.".loge()
        }
        postInvalidate()
    }

    /**
     * ai思考点位，15进制数表示
     */
    override fun aiThinkPoint(point: Int) {
        mThinkPoint = point
        postInvalidate()
    }

    private fun checkGameOver4() {
        for (index in mChessList.withIndex()) {
            val value = index.value
            "foreach chessList index: $value".logi()
            if (mChessList[value] == 0) return
            if (value == mChessList.size) {
                mIsGameOver = true
                mGameState = GomokuGameState.DREW
                mGameCallBack?.onGameOver(GomokuGameState.DREW)
            }
            val point = mChessList[value]
            val x = point / mGomokuSize
            val y = point % mGomokuSize
            if (isFiveSame(mGomokuSize, x, y)) {
                mIsGameOver = true
                mGameState = if (mChessArray[x][y] == 2) GomokuGameState.WHITE_WIN
                else GomokuGameState.BLACK_WIN
                mGameCallBack?.onGameOver(mGameState)
            }
        }
    }

    private fun checkGameOver(size: Int, x: Int, y: Int) {
        val src = mChessArray[x][y]
        val state = if (src == 1) GomokuGameState.BLACK_WIN else GomokuGameState.WHITE_WIN
        //横向
        var i = 1
        var count = 0
        while (x - i > 0 && i < 5 && mChessArray[x - i][y] == src) {
            count++
            i++
        }
        i = 1
        while (x + i < size - 1 && i < 5 && mChessArray[x + i][y] == src) {
            count++
            i++
        }
        if (count == 4) {
            notifyEnding(state)
            return
        }

        //纵向
        i = 1
        count = 0
        while (y - i > 0 && i < 5 && mChessArray[x][y - i] == src) {
            count++
            i++
        }
        i = 1
        while (y + i < size - 1 && i < 5 && mChessArray[x][y + i] == src) {
            count++
            i++
        }
        if (count == 4) {
            notifyEnding(state)
            return
        }


        //斜向\
        i = 1
        count = 0
        while (x - i > 0 && y - i > 0 && i < 5 && mChessArray[x - i][y - i] == src) {
            count++
            i++
        }
        i = 1
        while (x + i < size - 1 && y + i < size - 1 && i < 5 && mChessArray[x + i][y + i] == src) {
            count++
            i++
        }
        if (count == 4) {
            notifyEnding(state)
            return
        }

        //斜向/
        i = 1
        count = 0
        while (x + i < size - 1 && y - i > 0 && i < 5 && mChessArray[x + i][y - i] == src) {
            count++
            i++
        }
        i = 1
        while (x - i > 0 && y + i < size - 1 && i < 5 && mChessArray[x - i][y + i] == src) {
            count++
            i++
        }
        if (count == 4) {
            notifyEnding(state)
            return
        }

        if (mChessList[mChessList.size - 1] != 0) notifyEnding(GomokuGameState.DREW)  //和局
    }


    /**
     * 检查游戏是否结束
     */
    private fun checkGameOver5() {
        var i = 0
        var isDrew = true
        val size = mGomokuSize

        while (i < size) {
            var j = 0
            while (j < size) {
                var temp = isDrew

                if (mChessArray[i][j] == 0 || mChessArray[i][j] == 1) {
                    temp = false
                }

                if ((mChessArray[i][j] == 2 || mChessArray[i][j] == 1) && isFiveSame(size, i, j)) {
                    mIsGameOver = true
                    if (mChessArray[i][j] == 2) notifyEnding(GomokuGameState.WHITE_WIN)
                    else notifyEnding(GomokuGameState.BLACK_WIN)
                    return
                } else {
                    ++j
                    isDrew = temp
                }
            }
            ++i
        }
        if (isDrew) {
            mIsGameOver = true
            notifyEnding(GomokuGameState.DREW)
        }
    }

    /**
     * 是否五子连珠
     */
    private fun isFiveSame(size: Int, x: Int, y: Int): Boolean {
        //向右检查
        val n3 = x + 4
        if (n3 < size && mChessArray[x][y] == mChessArray[x + 1][y] &&
            mChessArray[x][y] == mChessArray[x + 2][y] &&
            mChessArray[x][y] == mChessArray[x + 3][y] &&
            mChessArray[x][y] == mChessArray[n3][y]) {
            return true
        }
        //向下检查
        val n4 = y + 4
        if (n4 < size && mChessArray[x][y] == mChessArray[x][y + 1] &&
            mChessArray[x][y] == mChessArray[x][y + 2] &&
            mChessArray[x][y] == mChessArray[x][y + 3] &&
            mChessArray[x][y] == mChessArray[x][n4]) {
            return true
        }
        //向右下检查
        if (n4 < size && n3 < size &&
            mChessArray[x][y] == mChessArray[x + 1][y + 1] &&
            mChessArray[x][y] == mChessArray[x + 2][y + 2] &&
            mChessArray[x][y] == mChessArray[x + 3][y + 3] &&
            mChessArray[x][y] == mChessArray[n3][n4]) {
            return true
        }
        //向右上检查
        val n5 = y - 4
        return n5 >= 0 && n3 < size &&
            mChessArray[x][y] == mChessArray[x + 1][y - 1] &&
            mChessArray[x][y] == mChessArray[x + 2][y - 2] &&
            mChessArray[x][y] == mChessArray[x + 3][y - 3] &&
            mChessArray[x][y] == mChessArray[n3][n5]
    }


    private fun doStone(whoTurn: Int, x: Int, y: Int, format: String) {
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            "DoStone $x $y out of range".logi()
            return
        }
        if (mChessArray[x][y] != 0) {
            "DoStone $x $y already set".logi()
            return
        }
        val s = if (whoTurn == 1) "黑" else "白"
        "$s${mStepNum + 1} @ ${convertX(x)}${convertY(y)} : $format".logi()

        val chessNum = mGomokuSize * x + y
        mChessList[mStepNum] = chessNum
        mChessArrayList.add(chessNum)

        if (mChessList[mStepNum] != mChessListFar[mStepNum]) {
            mChessListFar[mStepNum] = mChessList[mStepNum]
            mStepFarNum = mStepNum
            ++mStepFarNum
        }
        ++mStepNum
        mChessArray[x][y] = whoTurn
        mTurnWho = oppTurn(whoTurn)
        postInvalidate()
    }

    /**
     * 游戏结束统一处理
     * 只接受  WHITE_WIN   BLACK_WIN   DREW
     */
    private fun notifyEnding(state: GomokuGameState) {
        if (state == GomokuGameState.GOING) return
        mIsGameOver = true
        mGameState = state
        mGameCallBack?.onGameOver(state)

        val whoWin = when (state) {
            GomokuGameState.WHITE_WIN -> "White win. "
            GomokuGameState.BLACK_WIN -> "Black win. "
            GomokuGameState.DREW -> "Drew. "
            else -> "Error invoke notifyEnding()! "
        }
        val str = mChessArrayList.joinToString(", ")
        { "${convertX(it / mGomokuSize)}${convertY(it % mGomokuSize)}" }
        "$whoWin Print: $str".logi()
    }

    /**
     * 重置游戏状态值
     */
    private fun resetGameStateValue() {
        for (i in 0 until mGomokuSize) {
            for (j in 0 until mGomokuSize) {
                mChessArray[i][j] = 0
            }
        }
        mChessArrayList.clear()
        mIsGameOver = false
        mStepNum = 0
        mStepFarNum = 0
        mIsThinking = false
        mTurnWho = 1
        mThinkPoint = 0
        mGameState = GomokuGameState.GOING
    }

    private fun oppTurn(n: Int) = if (n == 1) 2 else 1
    private fun convertX(x: Int) = (x + 65).toChar().toString()
    private fun convertY(y: Int) = if (mGomokuSize - y < 10) (mGomokuSize - y).toString()
    else (87 + mGomokuSize - y).toChar().toString()

    fun aiRun() {
        "aiRun".logi()
        mIsThinking = true
        mGameCallBack?.onAiRunState(true)
        mAI?.aiBout()
    }

    /**
     * 设置游戏模式
     * @needAI true:人机模式 ,false:人人模式
     */
    fun setAI(needAi: Boolean) {
        mGameMode = needAi
    }

    /**
     * 悔棋
     */
    fun isCanGoBack() = !mIsThinking && mStepNum > 0

    fun goBack() {
        if (isCanGoBack()) {
            mTurnWho = oppTurn(mTurnWho)
            mChessArray[mChessList[mStepNum - 1] / mGomokuSize][mChessList[mStepNum - 1] % mGomokuSize] = 0
            mChessArrayList.removeAt(mChessArrayList.size - 1)
            --mStepNum
            mIsGameOver = false

            mAI?.aiTEFACES(AI.DO_UNDO, mTurnWho, 0, 0, mStepNum)
            mGameCallBack?.onChessChange()

            postInvalidate()
            return
        }
        if (mIsThinking) "Can`t go back. Thinking.".logi()
        if (mStepNum <= 0) "Can`t go back. None.".logi()
    }

    /**
     * 撤销悔棋
     */
    fun isCanGoForward() = !mIsGameOver && !mIsThinking && mStepFarNum > mStepNum

    fun goForward() {
        if (isCanGoForward()) {
            val n = mChessListFar[mStepNum] / mGomokuSize
            val n2 = mChessListFar[mStepNum] % mGomokuSize
            mChessArray[n][n2] = mTurnWho
            mTurnWho = oppTurn(mTurnWho)
            mChessList[mStepNum] = mChessListFar[mStepNum]
            mChessArrayList.add(mChessListFar[mStepNum])

            ++mStepNum

            mAI?.aiTEFACES(AI.DO_DOSTONE, oppTurn(mTurnWho), n, n2, mStepNum)
            //checkGameOver()
            checkGameOver(mGomokuSize, n, n2)
            mGameCallBack?.onChessChange()
            postInvalidate()
            return
        }
        if (mIsGameOver) "Can`t forward. GameOver.".logi()
        if (mIsThinking) "Can`t forward. Thinking.".logi()
        if (mStepFarNum <= mStepNum) "Can`t forward. None".logi()
    }

    /**
     * 重新开始游戏
     */
    fun newGame() {
        "GameNew".logi()
        resetGameStateValue()
        postInvalidate()
        mAI?.aiTEFACES(AI.DO_NEWGAME, 1, 0, 0, mStepNum)
        mAI?.aiTEFACES(AI.DO_STEPTIME, mTurnWho, 0, 0, mStepTime)
        mGameCallBack?.onChessChange()
        //this.aiTEFACES(do_DEBUG, FiveChessView2.turnWho, 0, 0, FiveChessView2.FIVE_DEBUG)
    }

    /**
     * 设置单步思考时间
     * 支持 2秒，5秒，10秒，60秒
     * @time 单位秒
     */
    fun setStepTime(time: Int) {
        mStepTime = time
        mAI?.aiTEFACES(AI.DO_STEPTIME, mTurnWho, 0, 0, mStepTime)
    }

    /**
     * 停止ai计算
     */
    fun stopAiRun() {
        mAI?.aiTEFACES(AI.DO_STOP, mTurnWho, 0, 0, mStepTime)
    }

    /**
     * 设置游戏状态监听
     */
    fun setGameCallBack(callBack: GameCallBack) {
        mGameCallBack = callBack

    }

    /* /**
     * 退出程序
     */
    public void CloseWin() {
        final int do_STOP = test.DO_STOP;
        final int turnWho = FiveChessView2.turnWho;
        this.aiTEFACES(do_STOP, turnWho, 0, 0, FiveChessView2.stepNum);
        FiveChessView2.FIVE_DEBUG = 0;
        final int do_DEBUG = test.DO_DEBUG;
        this.aiTEFACES(do_DEBUG, FiveChessView2.turnWho, 0, 0, 0);
        this.finish();
    }*/
}
