package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

class Tile : Cell {
    private var value = 0
    private var mergedFrom: Array<Tile?>? = null

    constructor(x: Int, y: Int, value: Int) : super(x, y) {
        this.value = value
    }

    constructor(cell: Cell?, value: Int) : super(cell?.getX(), cell?.getY()) {
        this.value = value
    }

    fun updatePosition(cell: Cell) {
        this.setX(cell.getX())
        this.setY(cell.getY())
    }

    fun getValue(): Int {
        return value
    }

    fun getMergedFrom(): Array<Tile?>? {
        return mergedFrom
    }

    fun setMergedFrom(tile: Array<Tile?>?) {
        mergedFrom = tile
    }
}