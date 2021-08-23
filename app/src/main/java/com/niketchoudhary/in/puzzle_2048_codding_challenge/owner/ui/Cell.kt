package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

open class Cell(private var x: Int?, private var y: Int?) {

    fun getX(): Int? {
        return x
    }

    fun setX(x: Int?) {
        this.x = x
    }

    fun getY(): Int? {
        return y
    }

    fun setY(y: Int?) {
        this.y = y
    }
}