package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui.*
import java.util.*

/*
* A class that holds the data related to game
*
* */
class GameModel(private val mContext: Context, view: GameView) {
    var gameState = GAME_NORMAL
    private val mView: GameView = view
    var grid: Grid? = null
    var aGrid: AnimationGrid? = null
    var score: Long = 0
    var mHighScore: Long = 0

    val isActive: Boolean
        get() = !(gameWon() || gameLost())

    fun setEndlessMode() {
        gameState = GAME_ENDLESS
        mView.invalidate()
        mView.refreshLastTime = true
    }

    fun gameWon(): Boolean {
        return gameState > 0 && gameState % 2 != 0
    }

    fun gameLost(): Boolean {
        return gameState == GAME_LOST
    }

    private fun getVector(direction: Int): Cell {
        val map: Array<Cell> = arrayOf(
            Cell(0, -1),  // up
            Cell(1, 0),  // right
            Cell(0, 1),  // down
            Cell(-1, 0) // left
        )
        return map[direction]
    }

    private fun buildTraversalsX(vector: Cell): List<Int> {
        val traversals: MutableList<Int> = ArrayList()
        val rows = 4
        for (xx in 0 until rows) traversals.add(xx)
        if (vector.getX() == 1) traversals.reverse()
        return traversals
    }

    private fun buildTraversalsY(vector: Cell): List<Int> {
        val traversals: MutableList<Int> = ArrayList()
        val rows = 4
        for (xx in 0 until rows) traversals.add(xx)
        if (vector.getY() == 1) traversals.reverse()
        return traversals
    }


    private fun prepareTiles() {
        for (array in grid?.field!!) for (tile in array) if (grid?.isCellOccupied(tile)!!) tile?.setMergedFrom(
            null
        )
    }

    private fun findFarthestPosition(cell: Cell, vector: Cell): Array<Cell> {
        var previous: Cell
        var nextCell = Cell(cell.getX(), cell.getY())
        do {
            previous = nextCell
            nextCell = Cell(
                previous.getX()!! + vector.getX()!!,
                previous.getY()!! + vector.getY()!!
            )
        } while (grid?.isCellWithinBounds(nextCell)!! && grid?.isCellAvailable(nextCell)!!)
        return arrayOf(previous, nextCell)
    }

    fun canContinue(): Boolean {
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON)
    }


    private fun winValue(): Int {
        return if (!canContinue()) endingMaxValue!! else startingMaxValue
    }

    private fun recordHighScore() {
        val rows = 4
        val settings = mContext.getSharedPreferences("4028", MODE_PRIVATE)
        val editor = settings.edit()
        editor.putLong(HIGH_SCORE + rows, mHighScore)
        editor.apply()
    }

    private fun endGame() {
        aGrid?.startAnimation(
            -1,
            -1,
            FADE_GLOBAL_ANIMATION,
            NOTIFICATION_ANIMATION_TIME,
            NOTIFICATION_DELAY_TIME,
            null
        )
        if (score >= mHighScore) {
            mHighScore = score
            recordHighScore()
        }
    }

    private fun tileMatchesAvailable(): Boolean {
        var tile: Tile?
        val rows = 4
        for (xx in 0 until rows) {
            for (yy in 0 until rows) {
                tile = grid?.getCellContent(Cell(xx, yy))
                tile?.let {
                    for (direction in 0..3) {
                        val vector: Cell = getVector(direction)
                        val cell = Cell(xx + vector.getX()!!, yy + vector.getY()!!)
                        val other: Tile? = grid?.getCellContent(cell)
                        if (other != null && other.getValue() == tile.getValue()) return true
                    }
                }
            }
        }
        return false
    }

    private fun movesAvailable(): Boolean {
        return grid?.isCellsAvailable()!! || tileMatchesAvailable()
    }

    private fun checkLose() {
        if (!movesAvailable() && !gameWon()) {
            gameState = GAME_LOST
            endGame()
        }
    }

    private fun moveTile(tile: Tile, cell: Cell) {
        grid?.field?.get(tile.getX()!!)?.set(tile.getY()!!, null)
        grid?.field?.get(cell.getX()!!)?.set(cell.getY()!!, tile)
        tile.updatePosition(cell)
    }

    private fun positionsEqual(first: Cell, second: Cell?): Boolean {
        return first.getX() === second?.getX() && first.getY() === second?.getY()
    }

    private fun spawnTile(tile: Tile) {
        grid?.insertTile(tile)
        aGrid?.startAnimation(
            tile.getX()!!, tile.getY()!!, SPAWN_ANIMATION,
            SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null
        ) //Direction: -1 = EXPANDING
    }

    private fun addRandomTile() {
        if (grid?.isCellsAvailable() == true) {
            val value = if (Math.random() < 0.9) 2 else 4
            val tile = Tile(grid?.randomAvailableCell(), value)
            spawnTile(tile)
        }
    }

    fun move(direction: Int) {
        aGrid?.cancelAnimations()
        // 0: up, 1: right, 2: down, 3: left

        if (!isActive) return

        val vector: Cell = getVector(direction)
        val traversalsX = buildTraversalsX(vector)
        val traversalsY = buildTraversalsY(vector)
        var moved = false
        prepareTiles()
        for (xx in traversalsX) {
            for (yy in traversalsY) {
                val cell = Cell(xx, yy)
                val tile: Tile? = grid?.getCellContent(cell)
                tile?.let {
                    val positions: Array<Cell> = findFarthestPosition(cell, vector)
                    val next: Tile? = grid?.getCellContent(positions[1])

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        val merged = Tile(positions[1], tile.getValue() * 2)
                        val temp: Array<Tile?> = arrayOf(tile, next)
                        merged.setMergedFrom(temp)
                        grid?.insertTile(merged)
                        grid?.removeTile(tile)

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1])
                        val extras = intArrayOf(xx, yy)
                        aGrid?.startAnimation(
                            merged.getX()!!, merged.getY()!!, MOVE_ANIMATION,
                            MOVE_ANIMATION_TIME, 0, extras
                        ) //Direction: 0 = MOVING MERGED
                        aGrid?.startAnimation(
                            merged.getX()!!, merged.getY()!!, MERGE_ANIMATION,
                            SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null
                        )

                        // Update the score
                        score += merged.getValue()
                        mHighScore = score.coerceAtLeast(mHighScore)

                        // The mighty 2048 tile
                        if (merged.getValue() >= winValue() && !gameWon()) {
                            gameState += GAME_WIN // Set win state
                            endGame()
                        }
                    } else {
                        moveTile(tile, positions[0])
                        val extras = intArrayOf(xx, yy, 0)
                        aGrid?.startAnimation(
                            positions[0].getX()!!,
                            positions[0].getY()!!,
                            MOVE_ANIMATION,
                            MOVE_ANIMATION_TIME,
                            0,
                            extras
                        ) //Direction: 1 = MOVING NO MERGE
                    }
                    if (!positionsEqual(cell, tile)) moved = true
                }
            }
        }
        if (moved) {
            addRandomTile()
            checkLose()
        }
        mView.reSyncTime()
        mView.invalidate()
    }

    private fun getHighScore(): Long {
        val rows = 4
        val settings = mContext.getSharedPreferences("4028", MODE_PRIVATE)
        return settings.getLong(HIGH_SCORE + rows, -1)
    }

    fun newGame() {
        val rows = 4
        if (grid == null) grid = Grid(rows, rows) else {
            grid?.clearGrid()
        }
        aGrid = AnimationGrid(rows, rows)
        mHighScore = getHighScore()
        if (score >= mHighScore) {
            mHighScore = score
            recordHighScore()
        }
        score = 0
        gameState = GAME_NORMAL
        addStartTiles()
        mView.refreshLastTime = true
        mView.reSyncTime()
        mView.invalidate()
    }

    private fun addStartTiles() {
        val startTiles = 2
        for (xx in 0 until startTiles) addRandomTile()
    }

    companion object {
        //Odd state = game is not active
        //Even state = game is active
        //Win state = active state + 1
        private const val GAME_WIN = 1
        private const val GAME_LOST = -1
        private const val GAME_NORMAL = 0
        private const val GAME_ENDLESS = 2
        private const val GAME_ENDLESS_WON = 3

        const val MOVE_ANIMATION = 0
        const val MERGE_ANIMATION = 1
        const val SPAWN_ANIMATION = -1
        private val MOVE_ANIMATION_TIME: Long = GameView.BASE_ANIMATION_TIME
        private val SPAWN_ANIMATION_TIME: Long = GameView.BASE_ANIMATION_TIME
        private val NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME
        private val NOTIFICATION_ANIMATION_TIME: Long = GameView.BASE_ANIMATION_TIME * 5
        private const val startingMaxValue = 2048
        private var endingMaxValue: Int? = null
        private const val HIGH_SCORE = "high score"
        const val FADE_GLOBAL_ANIMATION = 0
    }
}