package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

class AnimationCell(
    x: Int?,
    y: Int?,
    private var animationType: Int,
    length: Long,
    delay: Long,
    var extras: IntArray?
) : Cell(x, y) {
    private var animationTime: Long = length
    private var delayTime: Long = delay
    private var timeElapsed: Long = 0

    fun getAnimationType(): Int {
        return animationType
    }

    fun tick(timeElapsed: Long) {
        this.timeElapsed = this.timeElapsed + timeElapsed
    }

    fun animationDone(): Boolean {
        return animationTime + delayTime < timeElapsed
    }

    fun getPercentageDone(): Double {
        return 0.0.coerceAtLeast(1.0 * (timeElapsed - delayTime) / animationTime)
    }

    fun isActive(): Boolean {
        return timeElapsed >= delayTime
    }
}