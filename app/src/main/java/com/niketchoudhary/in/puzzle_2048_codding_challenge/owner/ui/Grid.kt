package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

class Grid(sizeX: Int, sizeY: Int) {
    var field: Array<Array<Tile?>> = Array(sizeX) { arrayOfNulls(sizeY) }

    init {
        clearGrid()
    }

    fun clearGrid() {
        for (xx in field.indices) for (yy in field[0].indices) field[xx][yy] = null
    }
}