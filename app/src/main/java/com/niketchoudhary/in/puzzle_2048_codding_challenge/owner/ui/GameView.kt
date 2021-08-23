package com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.R
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.data.GameModel
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.helper.SlideListener
import com.niketchoudhary.`in`.puzzle_2048_codding_challenge.owner.activity.GameActivity
import java.lang.Exception

class GameView : View {

    val numCellTypes = 21
    private val bitmapCell = arrayOfNulls<BitmapDrawable>(numCellTypes)
    val game: GameModel

    //Icons
    var sYIcons = 0
    var sXNewGame = 0
    var sXUndo = 0
    var iconSize = 0
    //Internal variables
    private val paint = Paint()
    var hasSaveState = false
    var continueButtonEnabled = false
    var startingX = 0
    var startingY = 0
    var endingX = 0
    var endingY = 0

    //Misc
    var refreshLastTime = true

    //Timing
    private var lastFPSTime = System.nanoTime()

    //Text
    private var titleTextSize = 0f
    private var bodyTextSize = 0f
    private var headerTextSize = 0f
    private var gameOverTextSize = 0f

    //Layout variables
    private var cellSize = 0
    private var textSize = 0f
    private var cellTextSize = 0f
    private var gridWidth = 0
    private var textPaddingSize = 0
    private var iconPaddingSize = 0

    //Assets
    private var backgroundRectangle: Drawable? = null
    private var lightUpRectangle: Drawable? = null
    private var fadeRectangle: Drawable? = null
    private var background: Bitmap? = null
    private var loseGameOverlay: BitmapDrawable? = null
    private var winGameContinueOverlay: BitmapDrawable? = null
    private var winGameFinalOverlay: BitmapDrawable? = null

    //Text variables
    private var sYAll = 0
    private var titleStartYAll = 0
    private var bodyStartYAll = 0
    private var eYAll = 0
    private var titleWidthHighScore = 0
    private var titleWidthScore = 0
    var mActivity: GameActivity? = null
    var mContext: Context

    constructor(context: Context, activity: GameActivity?) : super(context) {
        mActivity = activity
        mContext = context

        //Loading resources
        game = GameModel(context, this)
        try {
            //Getting assets
            backgroundRectangle = getDrawable(R.drawable.background_rectangle)
            lightUpRectangle = getDrawable(R.drawable.light_up_rectangle)
            fadeRectangle = getDrawable(R.drawable.fade_rectangle)
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground))
            val font = Typeface.createFromAsset(resources.assets, "ClearSans-Bold.ttf")
            paint.typeface = font
            paint.isAntiAlias = true
        } catch (e: Exception) {
            Log.e(TAG, "Error getting assets?", e)
        }
        setOnTouchListener(SlideListener(this))
        game.newGame()
    }

    private fun getDrawable(resId: Int): Drawable {
        return resources.getDrawable(resId)
    }


    fun resyncTime() {
        lastFPSTime = System.nanoTime()
    }


    companion object{
        const val BASE_ANIMATION_TIME = 100000000L
        private val TAG = GameView::class.java.simpleName
    }
}