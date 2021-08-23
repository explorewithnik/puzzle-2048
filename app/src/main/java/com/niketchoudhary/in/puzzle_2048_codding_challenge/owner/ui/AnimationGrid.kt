package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

import java.util.ArrayList

class AnimationGrid(x: Int, y: Int) {
    var field: Array<Array<ArrayList<AnimationCell>?>> = Array(x) {
        arrayOfNulls(y)
    }
    val globalAnimation: ArrayList<AnimationCell> = ArrayList<AnimationCell>()

    private var activeAnimations = 0
    private var oneMoreFrame = false

    fun startAnimation(
        x: Int,
        y: Int,
        animationType: Int,
        length: Long,
        delay: Long,
        extras: IntArray?
    ) {
        val animationToAdd = AnimationCell(x, y, animationType, length, delay, extras)
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd)
        } else {
            field[x][y]?.add(animationToAdd)
        }
        activeAnimations += 1
    }

    fun tickAll(timeElapsed: Long) {
        val cancelledAnimations: ArrayList<AnimationCell> = ArrayList<AnimationCell>()
        for (animation in globalAnimation) {
            animation.tick(timeElapsed)
            if (animation.animationDone()) {
                cancelledAnimations.add(animation)
                activeAnimations -= 1
            }
        }
        for (array in field) {
            for (list in array) {
                list?.let {
                    for (animation in it) {
                        animation.tick(timeElapsed)
                        if (animation.animationDone()) {
                            cancelledAnimations.add(animation)
                            activeAnimations -= 1
                        }
                    }
                }
            }
        }
        for (animation in cancelledAnimations) {
            cancelAnimation(animation)
        }
    }

    private fun cancelAnimation(animation: AnimationCell) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation)
        } else {
            field[animation.getX()!!][animation.getY()!!]?.remove(animation)
        }
    }

    fun isAnimationActive(): Boolean {
        return when {
            activeAnimations != 0 -> {
                oneMoreFrame = true
                true
            }
            oneMoreFrame -> {
                oneMoreFrame = false
                true
            }
            else -> {
                false
            }
        }
    }

    fun getAnimationCell(x: Int, y: Int): ArrayList<AnimationCell>? {
        return field[x][y]
    }

    fun cancelAnimations() {
        for (array in field) {
            for (list in array) {
                list?.clear()
            }
        }
        globalAnimation.clear()
        activeAnimations = 0
    }
}