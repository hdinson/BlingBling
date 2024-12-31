package dinson.customview.widget._026fivechess

interface GameCallBack {

    fun onChessChange()
    fun onAiRunState(isStart: Boolean)
    fun onGameOver(state: GomokuGameState)

}
