package dinson.customview.weight._026fivechess

interface GameCallBack {

    fun onChessChange()
    fun onAiRunState(isStart: Boolean)
    fun onGameOver(state: GomokuGameState)

}
