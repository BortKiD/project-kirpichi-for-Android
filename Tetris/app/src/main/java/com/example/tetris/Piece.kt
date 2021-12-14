package com.example.tetris

class Piece {

    var pieceRow = 4
    var pieceCol = 4

    var px = 1
    var py = 11

    var type: Int = 0

    constructor(type: Int){
        this.type = type
        createPiece(type)
    }

    var piece = Array(pieceRow) {
        Array(pieceCol){0}
    }



    fun createPiece(type:Int){

        when(type){
            //I-block
            1 -> {
                this.piece[1][0] = 1
                this.piece[1][1] = 1
                this.piece[1][2] = 1
                this.piece[1][3] = 1
            }
            //O-block
            2 -> {
                this.piece[1][1] = 1
                this.piece[1][2] = 1
                this.piece[2][1] = 1
                this.piece[2][2] = 1
            }
            //T-block
            3 -> {
                this.piece[1][0] = 1
                this.piece[1][1] = 1
                this.piece[1][2] = 1
                this.piece[2][1] = 1
            }
            //J-block
            4 -> {
                this.piece[1][0] = 1
                this.piece[2][0] = 1
                this.piece[2][1] = 1
                this.piece[2][2] = 1
            }
            //L-block
            5 -> {
                this.piece[2][0] = 1
                this.piece[2][1] = 1
                this.piece[2][2] = 1
                this.piece[1][2] = 1
            }
            //S-block
            6 -> {
                this.piece[2][0] = 1
                this.piece[2][1] = 1
                this.piece[1][1] = 1
                this.piece[1][2] = 1
            }
            //Z-block
            7 -> {
                this.piece[1][0] = 1
                this.piece[1][1] = 1
                this.piece[2][1] = 1
                this.piece[2][2] = 1
            }
        }

    }

    fun isCorrectMoveRight(board: Array<Array<Int>>):Boolean{
        for(i in 0 until pieceRow){
            for(j in 0 until pieceCol){
                if(this.piece[i][j] == 1){
                    if(board[this.px+i][this.py+j+1] != 0)
                        return false
                }
            }
        }

        return true
    }

    fun moveRight(){
        py++
    }

    fun isCorrectMoveLeft(board: Array<Array<Int>>):Boolean{
        for(i in 0 until pieceRow){
            for(j in 0 until pieceCol){
                if(this.piece[i][j] == 1){
                    if(board[this.px+i][this.py+j-1] != 0)
                        return false
                }
            }
        }

        return true
    }

    fun moveLeft(){
        py--
    }

    fun isCorrectMoveDown(board:Array<Array<Int>>): Boolean {

        for (i in 0 until pieceRow) {
            for (j in 0 until pieceCol) {
                if (this.piece[i][j] == 1) {
                    if (board[this.px+i+1][this.py+j] != 0) {
                        return false
                    }
                }
            }
        }

        return true
    }

    fun moveDown(){
      this.px++
    }

    fun isCollide(board:Array<Array<Int>>):Boolean{

        for (i in 0 until pieceRow) {
            for (j in 0 until pieceCol) {
                if (this.piece[i][j] == 1) {
                    if (isCorrectMoveRight(board) && isCorrectMoveLeft(board) && isCorrectMoveDown(board)) {
                        if (board[this.px+i+2][this.py+j] != 0
                                || board[this.px+i][this.py+j-2] != 0
                                || board[this.px+i][this.py+j+2] != 0) {
                            return false
                        }
                    } else {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun isCorrectRotate(board:Array<Array<Int>>) {

        if(this.type != 1){
            if(isCorrectMoveRight(board) && isCorrectMoveLeft(board) && isCorrectMoveDown(board)){
                rotatePiece()
            }
        } else {
            if(isCollide(board)){
                rotatePiece()
            }
        }
    }

    fun rotatePiece() {

        var temp = Array(pieceRow) {
            Array(pieceCol) { 0 }
        }

        for (i in 0 until pieceRow) {
            for ((aux, j) in (pieceCol - 1 downTo 0).withIndex()) {
                temp[i][aux] = piece[j][i]
            }
        }

        piece = temp
    }

    fun drawPiece(board:Array<Array<Int>>):Array<Array<Int>> {
        for(i in 0 until pieceRow) {
            for (j in 0 until pieceCol) {
                if(this.piece[i][j] == 1) {
                    board[this.px+i][this.py+j] = 2
                }
            }
        }

        return board
    }
}