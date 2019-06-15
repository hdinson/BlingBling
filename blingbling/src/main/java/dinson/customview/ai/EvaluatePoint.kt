package dinson.customview.ai

/**
 * 启发式评价函数
 * 这个是专门给某一个位置打分的，不是给整个棋盘打分的
 * 并且是只给某一个角色打分
 */
object EvaluatePoint {


    /**
     * 表示在当前位置下一个棋子后的分数
     * 为了性能考虑，增加了一个dir参数，如果没有传入则默认计算所有四个方向，如果传入值，则只计算其中一个方向的值
     *
     * evaluate
     */
    fun s(b: Board, px: Int, py: Int, role: Int, dir: Int = 0): Int {

        var board = b.board
        var result = 0
        val radius = 8
        var empty = 0
        var count = 0
        var block = 0
        var secondCount = 0  //另一个方向的count

        val len = board.size

        fun reset() {
            count = 1
            block = 0
            empty = -1
            secondCount = 0  //另一个方向的count
        }

        if (dir == 0) {
            reset()

            var i = py + 1
            while (true) {
                if (i >= len) {
                    block++
                    break
                }
                val t = board[px][i]

                if (t == Role.empty) {
                    if (empty == -1 && i < len - 1 && board[px][i + 1] == role) {
                        empty = count
                        i++
                        continue
                    } else {
                        break
                    }
                }

                if (t == role) {
                    count++
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }

            i = py - 1
            while (true) {
                if (i < 0) {
                    block++
                    break
                }
                val t = board[px][i]
                if (t == Role.empty) {
                    if (empty == -1 && i > 0 && board[px][i - 1] == role) {
                        empty = 0  //注意这里是0，因为是从右往左走的
                        i--
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    secondCount++
                    if (empty != -1) {
                        empty++
                    }  //注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                    i--
                    continue
                } else {
                    block++
                    break
                }
            }
            count += secondCount
            b.scoreCache[role][0][px][py] = countToScore(count, block, empty)
        }
        result += b.scoreCache[role][0][px][py]

        if (dir == 0 || dir == 1) {
            reset()
            var i = px + 1
            while (true) {
                if (i >= len) {
                    block++
                    break
                }
                val t = board[i][py]
                if (t == Role.empty) {
                    if (empty == -1 && i < len - 1 && board[i + 1][py] == role) {
                        empty = count
                        i++
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    count++
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }
            i = px - 1
            while (true) {
                if (i < 0) {
                    block++
                    break
                }
                val t = board[i][py]
                if (t == Role.empty) {
                    if (empty == -1 && i > 0 && board[i - 1][py] == role) {
                        empty = 0
                        i--
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    secondCount++
                    if (empty != -1) {
                        empty++
                    }  //注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                    i--
                    continue
                } else {
                    block++
                    break
                }
            }
            count += secondCount
            b.scoreCache[role][1][px][py] = countToScore(count, block, empty)
        }
        result += b.scoreCache[role][1][px][py]

        if (dir == 0 || dir == 2) {
            reset()
            var i = 1
            while (true) {
                val x = px + i
                val y = py + i
                if (x >= len || y >= len) {
                    block++
                    break
                }
                val t = board[x][y]
                if (t == Role.empty) {
                    if (empty == -1 && (x < len - 1 && y < len - 1) && board[x + 1][y + 1] == role) {
                        empty = count
                        i++
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    count++
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }

            i = 1
            while (true) {
                val x = px - i
                val y = py - i
                if (x < 0 || y < 0) {
                    block++
                    break
                }
                val t = board[x][y]
                if (t == Role.empty) {
                    if (empty == -1 && (x > 0 && y > 0) && board[x - 1][y - 1] == role) {
                        empty = 0
                        i++
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    secondCount++
                    if (empty != -1) {
                        empty++
                    }  //注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }
            count += secondCount
            b.scoreCache[role][2][px][py] = countToScore(count, block, empty)
        }
        result += b.scoreCache[role][2][px][py]

        if (dir == 0 || dir == 3) {
            reset()
            var i = 1
            while (true) {
                val x = px + i
                val y = py - i
                if (x < 0 || y < 0 || x >= len || y >= len) {
                    block++
                    break
                }
                val t = board[x][y]
                if (t == Role.empty) {
                    if (empty == -1 && (x < len - 1 && y < len - 1) && board[x + 1][y - 1] == role) {
                        empty = count
                        i++
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    count++
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }
            i = 1
            while (true) {
                val x = px - i
                val y = py + i
                if (x < 0 || y < 0 || x >= len || y >= len) {
                    block++
                    break
                }
                val t = board[x][y]
                if (t == Role.empty) {
                    if (empty == -1 && (x > 0 && y > 0) && board[x - 1][y + 1] == role) {
                        empty = 0
                        i++
                        continue
                    } else {
                        break
                    }
                }
                if (t == role) {
                    secondCount++
                    if (empty != -1) {
                        empty++
                    }  //注意这里，如果左边又多了己方棋子，那么empty的位置就变大了
                    i++
                    continue
                } else {
                    block++
                    break
                }
            }
            count += secondCount
            b.scoreCache[role][3][px][py] = countToScore(count, block, empty)
        }
        result += b.scoreCache[role][3][px][py]

        return result
    }


    private fun countToScore(count: Int, block: Int, empty: Int = 0): Int {
        //没有空位
        if (empty <= 0) {
            if (count >= 5) return ChessType.FIVE.score
            if (block == 0) {
                when (count) {
                    1 -> return ChessType.ONE.score
                    2 -> return ChessType.TWO.score
                    3 -> return ChessType.THREE.score
                    4 -> return ChessType.FOUR.score
                }
            }
            if (block == 1) {
                when (count) {
                    1 -> return ChessType.BLOCKED_ONE.score
                    2 -> return ChessType.BLOCKED_TWO.score
                    3 -> return ChessType.BLOCKED_THREE.score
                    4 -> return ChessType.BLOCKED_FOUR.score
                }
            }
        } else if (empty == 1 || empty == count - 1) {
            //第1个是空位
            if (count >= 6) {
                return ChessType.FIVE.score
            }
            if (block == 0) {
                when (count) {
                    2 -> return ChessType.TWO.score / 2
                    3 -> return ChessType.THREE.score
                    4 -> return ChessType.BLOCKED_FOUR.score
                    5 -> return ChessType.FOUR.score
                }
            }

            if (block == 1) {
                when (count) {
                    2 -> return ChessType.BLOCKED_TWO.score
                    3 -> return ChessType.BLOCKED_THREE.score
                    4 -> return ChessType.BLOCKED_FOUR.score
                    5 -> return ChessType.BLOCKED_FOUR.score
                }
            }
        } else if (empty == 2 || empty == count - 2) {
            //第二个是空位
            if (count >= 7) {
                return ChessType.FIVE.score
            }
            if (block == 0) {
                when (count) {
                    3 -> return ChessType.THREE.score
                    4, 5 -> return ChessType.BLOCKED_FOUR.score
                    6 -> return ChessType.FOUR.score
                }
            }

            if (block == 1) {
                when (count) {
                    3 -> return ChessType.BLOCKED_THREE.score
                    4 -> return ChessType.BLOCKED_FOUR.score
                    5 -> return ChessType.BLOCKED_FOUR.score
                    6 -> return ChessType.FOUR.score
                }
            }

            if (block == 2) {
                when (count) {
                    4, 5, 6 -> return ChessType.BLOCKED_FOUR.score
                }
            }
        } else if (empty == 3 || empty == count - 3) {
            if (count >= 8) {
                return ChessType.FIVE.score
            }
            if (block == 0) {
                when (count) {
                    4, 5 -> return ChessType.THREE.score
                    6 -> return ChessType.BLOCKED_FOUR.score
                    7 -> return ChessType.FOUR.score
                }
            }

            if (block == 1) {
                when (count) {
                    4, 5, 6 -> return ChessType.BLOCKED_FOUR.score
                    7 -> return ChessType.FOUR.score
                }
            }

            if (block == 2) {
                when (count) {
                    4, 5, 6, 7 -> return ChessType.BLOCKED_FOUR.score
                }
            }
        } else if (empty == 4 || empty == count - 4) {
            if (count >= 9) {
                return ChessType.FIVE.score
            }
            if (block == 0) {
                when (count) {
                    5, 6, 7, 8 -> return ChessType.FOUR.score
                }
            }

            if (block == 1) {
                when (count) {
                    4, 5, 6, 7 -> return ChessType.BLOCKED_FOUR.score
                    8 -> return ChessType.FOUR.score
                }
            }

            if (block == 2) {
                when (count) {
                    5, 6, 7, 8 -> return ChessType.BLOCKED_FOUR.score
                }
            }
        } else if (empty == 5 || empty == count - 5) {
            return ChessType.FIVE.score
        }

        return 0
    }
}