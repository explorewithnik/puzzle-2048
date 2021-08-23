package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

import java.util.ArrayList
import kotlin.math.floor

open class Grid(sizeX: Int, sizeY: Int) {
    var field: Array<Array<Tile?>> = Array(sizeX) { arrayOfNulls(sizeY) }

    init {
        clearGrid()
    }

    fun clearGrid() {
        for (xx in field.indices) for (yy in field[0].indices) field[xx][yy] = null
    }

    fun randomAvailableCell(): Cell? {
        val availableCells: ArrayList<Cell> = getAvailableCells()
        return if (availableCells.size >= 1) availableCells[floor(Math.random() * availableCells.size)
            .toInt()] else null
    }

    fun getAvailableCells(): ArrayList<Cell> {
        val availableCells = ArrayList<Cell>()
        for (xx in field.indices) for (yy in field[0].indices) if (field[xx][yy] == null) availableCells.add(
            Cell(xx, yy)
        )
        return availableCells
    }

    fun isCellsAvailable(): Boolean {
        return getAvailableCells().size >= 1
    }

    fun isCellAvailable(cell: Cell?): Boolean {
        return !isCellOccupied(cell)
    }

    fun isCellOccupied(cell: Cell?): Boolean {
        return getCellContent(cell) != null
    }

    fun getCellContent(cell: Cell?): Tile? {
        return if (cell != null && isCellWithinBounds(cell)) field[cell.getX()!!][cell.getY()!!] else null
    }

    fun getCellContent(x: Int, y: Int): Tile? {
        return if (isCellWithinBounds(x, y)) field[x][y] else null
    }

    fun isCellWithinBounds(cell: Cell): Boolean {
        return 0 <= cell.getX()!! && cell.getX()!! < field.size && 0 <= cell.getY()!! && cell.getY()!! < field[0].size
    }

    open fun isCellWithinBounds(x: Int, y: Int): Boolean {
        return 0 <= x && x < field.size && 0 <= y && y < field[0].size
    }

    fun insertTile(tile: Tile) {
        field[tile.getX()!!][tile.getY()!!] = tile
    }

    fun removeTile(tile: Tile) {
        field[tile.getX()!!][tile.getY()!!] = null
    }
}