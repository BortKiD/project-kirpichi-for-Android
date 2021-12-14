package com.example.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.example.tetris.databinding.ActivityBoardBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    val row = 36
    val column = 26
    var start = true
    var speed: Long = 300
    var go = 0

    var top = 0
    var bottom = 0
    var right = 0
    var left = 0

    var isFullLine = 0

    lateinit var binding: ActivityBoardBinding

    var piece = Piece(1)

    var board = Array(row) {
        Array(column){0}
    }

    var boardView = Array(row) {
        arrayOfNulls<ImageView>(column)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board)

        binding.moveLeftButton.setOnClickListener {
            if(piece.isCorrectMoveLeft(board)) {
                piece.moveLeft()
            }
        }

        binding.moveRightButton.setOnClickListener {
            if(piece.isCorrectMoveRight(board)) {
                piece.moveRight()
            }
        }

        binding.moveDownButton.setOnClickListener {
            if(piece.isCorrectMoveDown(board)){
                piece.moveDown()
            }
        }

        binding.rotateButton.setOnClickListener {
            piece.isCorrectRotate(board)
        }

        var gridBoard = findViewById<GridLayout>(R.id.gridBoard)
        gridBoard.rowCount = row
        gridBoard.columnCount = column

        val inflater = LayoutInflater.from(this)

        for (i in 0 until row) {
            for (j in 0 until column) {
                boardView[i][j] = inflater.inflate(R.layout.inflate_image_view, gridBoard, false) as ImageView
                gridBoard.addView(boardView[i][j])
            }
        }

        processGameEvents()
    }

    fun getBorder(piece: Piece) {
        for (i in 0..3) for (j in 0..3) if (piece.piece[i][j] == 1) {
            bottom = i
            break
        }
        for (i in 3 downTo 0) for (j in 0..3) if (piece.piece[i][j] == 1) {
            top = i
            break
        }
        for (j in 0..3) for (i in 0..3) if (piece.piece[i][j] == 1) {
            right = j
            break
        }
        for (j in 3 downTo 0) for (i in 0..3) if (piece.piece[i][j] == 1) {
            left = j
            break
        }
    }

    fun processGameEvents() {
        Thread {
            while (start) {
                Thread.sleep(speed)
                runOnUiThread {
                    getBorder(piece)

                    for (i in 0 until row) {
                        for (j in 0 until column) {
                                if ((i == 0 || i == row - 1) || (j == 0 || j == column - 1) || board[i][j] == 2) {
                                    boardView[i][j]!!.setImageResource(R.drawable.stable)
                                    board[i][j] = 2
                                } else if (board[i][j] == 1) {
                                    boardView[i][j]!!.setImageResource(R.drawable.falling)
                                } else {
                                    boardView[i][j]!!.setImageResource(R.drawable.back)
                                }
                        }
                    }

                    if (piece.isCorrectMoveDown(board)) {
                        piece.moveDown()
                    } else {
                        board = piece.drawPiece(board)
                        piece = Piece(Random.nextInt(1, 8))
                    }

                    // Broken plan A
                    for (i in 0 until piece.pieceRow) {
                        for (j in 0 until piece.pieceCol) {
                            if (piece.py + bottom == row - 1) {
                                start = false
                                break
                            }
                            if (piece.piece[i][j] == 1) {
                                boardView[piece.px + i][piece.py + j]!!.setImageResource(R.drawable.falling)
                            }
                        }
                    }

                    var i = row - 2
                    isFullLine = 0
                    while (i >= 2) {
                        for (j in 1 until column - 1)
                            if (board[i][j] == 0) {
                                isFullLine = 0
                                i--
                                break
                            }
                        if (isFullLine == 1) {
                            for (k in i downTo 2)
                                for (j in 1 until column - 1)
                                    board[k][j] = board[k - 1][j]
                        }
                    }

                    for (i in 1 until column - 1) {
                        if (board[2][i] == 2) {
                            go = 1
                            start = false
                            finish()
                        }
                    }
                }
            }
        }.start()

        if (go == 1)
            this.finish()
    }
}