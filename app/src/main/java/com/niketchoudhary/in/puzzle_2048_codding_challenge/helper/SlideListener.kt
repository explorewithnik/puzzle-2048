package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.helper

import android.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.R
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui.GameView
import kotlin.math.abs

/*
* A custom View.OnTouchListener class that will use the functionality of View's OnTouchListener
* to detect the user slide actions or arrow keys action perform
*
* */
class SlideListener (view: GameView?): View.OnTouchListener {

    private val SWIPE_MIN_DISTANCE = 0
    private val SWIPE_THRESHOLD_VELOCITY = 25
    private val MOVE_THRESHOLD = 250
    private val RESET_STARTING = 10
    private var mView: GameView? = view
    private var x = 0f
    private var y = 0f
    private var lastDx = 0f
    private var lastDy = 0f
    private var previousX = 0f
    private var previousY = 0f
    private var startingX = 0f
    private var startingY = 0f
    private var previousDirection = 1
    private var veryLastDirection = 1
    private var hasMoved = false

    private fun pathMoved(): Float {
        return (x - startingX) * (x - startingX) + (y - startingY) * (y - startingY)
    }

    private fun iconPressed(sx: Int?, sy: Int?): Boolean {
        return (isTap(1) && inRange(sx?.toFloat()!!, x, sx + mView?.iconSize?.toFloat()!!)
                && inRange(sy?.toFloat()!!, y, sy + mView?.iconSize?.toFloat()!!))
    }

    private fun inRange(starting: Float, check: Float, ending: Float): Boolean {
        return check in starting..ending
    }

    private fun isTap(factor: Int): Boolean {
        return pathMoved() <= mView!!.iconSize * factor
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.x
                y = event.y
                startingX = x
                startingY = y
                previousX = x
                previousY = y
                lastDx = 0f
                lastDy = 0f
                hasMoved = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.x
                y = event.y
                if (mView?.game?.isActive == true) {
                    val dx = x - previousX
                    if (abs(lastDx + dx) < abs(lastDx) + abs(dx) && abs(dx) > RESET_STARTING && abs(
                            x - startingX
                        ) > SWIPE_MIN_DISTANCE
                    ) {
                        startingX = x
                        startingY = y
                        lastDx = dx
                        previousDirection = veryLastDirection
                    }
                    if (lastDx == 0f) {
                        lastDx = dx
                    }
                    val dy = y - previousY
                    if (abs(lastDy + dy) < abs(lastDy) + abs(dy) && abs(dy) > RESET_STARTING && abs(
                            y - startingY
                        ) > SWIPE_MIN_DISTANCE
                    ) {
                        startingX = x
                        startingY = y
                        lastDy = dy
                        previousDirection = veryLastDirection
                    }
                    if (lastDy == 0f) {
                        lastDy = dy
                    }
                    if (pathMoved() > SWIPE_MIN_DISTANCE * SWIPE_MIN_DISTANCE && !hasMoved) {
                        var moved = false
                        //Vertical
                        if ((dy >= SWIPE_THRESHOLD_VELOCITY && abs(dy) >= abs(dx) || y - startingY >= MOVE_THRESHOLD) && previousDirection % 2 != 0) {
                            moved = true
                            previousDirection *= 2
                            veryLastDirection = 2
                            mView?.game?.move(2)
                        } else if ((dy <= -SWIPE_THRESHOLD_VELOCITY && abs(dy) >= abs(dx) || y - startingY <= -MOVE_THRESHOLD) && previousDirection % 3 != 0) {
                            moved = true
                            previousDirection *= 3
                            veryLastDirection = 3
                            mView?.game?.move(0)
                        }
                        //Horizontal
                        if ((dx >= SWIPE_THRESHOLD_VELOCITY && abs(dx) >= abs(dy) || x - startingX >= MOVE_THRESHOLD) && previousDirection % 5 != 0) {
                            moved = true
                            previousDirection *= 5
                            veryLastDirection = 5
                            mView?.game?.move(1)
                        } else if ((dx <= -SWIPE_THRESHOLD_VELOCITY && abs(dx) >= abs(dy) || x - startingX <= -MOVE_THRESHOLD) && previousDirection % 7 != 0) {
                            moved = true
                            previousDirection *= 7
                            veryLastDirection = 7
                            mView?.game?.move(3)
                        }
                        if (moved) {
                            hasMoved = true
                            startingX = x
                            startingY = y
                        }
                    }
                }
                previousX = x
                previousY = y
                return true
            }
            MotionEvent.ACTION_UP -> {
                x = event.x
                y = event.y
                previousDirection = 1
                veryLastDirection = 1

                //"Menu" inputs
                if (!hasMoved) {
                    if (iconPressed(mView?.sXNewGame, mView?.sYIcons)) {
                        AlertDialog.Builder(mView?.context)
                            .setPositiveButton(
                                R.string.reset
                            ) { _, _ -> // reset rewards again:
                                mView?.game?.newGame()
                            }
                            .setNegativeButton(R.string.continue_game, null)
                            .setTitle(R.string.reset_dialog_title)
                            .setMessage(R.string.reset_dialog_message)
                            .show()
                    } else if (isTap(2) && inRange(
                            mView?.startingX?.toFloat()!!,
                            x,
                            mView?.endingX?.toFloat()!!
                        )
                        && inRange(
                            mView?.startingY?.toFloat()!!,
                            x,
                            mView?.endingY?.toFloat()!!
                        ) && mView?.continueButtonEnabled!!
                    ) {
                        mView?.game?.setEndlessMode()
                    }
                }
            }
        }
        return true
    }
}