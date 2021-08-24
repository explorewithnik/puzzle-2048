package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.databinding.ActivityGameBinding
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui.GameView

class GameActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameBinding
    private var view: GameView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val mView = binding.root
        setContentView(mView)

        view = GameView(this, this)

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        view?.layoutParams = params

        binding.gameFrameLayout.addView(view)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> return true
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                view?.game?.move(2)
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                view?.game?.move(0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                view?.game?.move(3)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                view?.game?.move(1)
                return true
            }
            else -> return super.onKeyDown(keyCode, event)
        }
    }
}