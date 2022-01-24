package air.foi.hr.crop

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import java.util.logging.Logger
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import air.foi.hr.tool.Tool
import air.foi.hr.tool.UndoRedo

class CropTool(private val view: View, private val undoRedo: UndoRedo) : Tool(undoRedo = undoRedo) {
    private var paintRect = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 2.0f
    }
    private var paintCircle = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    var cropCallback: ((Float, Float, Float, Float) -> Unit)? = null

    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private var topCornerX = 10f
    private var topCornerY = 10f
    private var bottomCornerX = 200f
    private var bottomCornerY = 200f

    private enum class Corner {TOP, BOTTOM}
    private var movingCorner: Corner? = null


    private val touchTolerance = ViewConfiguration.get(view.context).scaledEdgeSlop

    private fun touchDown() {
        currentX = motionX
        currentY = motionY
        val distanceFromTopCorner = sqrt((topCornerX - currentX).pow(2) + (topCornerY - currentY).pow(2))
        val distanceFromBottomCorner = sqrt((bottomCornerX - currentX).pow(2) + (bottomCornerY - currentY).pow(2))
        // only allow moving when dragging close to edges
        movingCorner = if (min(distanceFromTopCorner, distanceFromBottomCorner) < 30.0f) {
            // determine which corner is being moved
            if (distanceFromTopCorner < distanceFromBottomCorner) {
                Corner.TOP
            } else {
                Corner.BOTTOM
            }
        } else {
            null
        }
    }

    private fun touchMove() {
        val dx = abs(motionX - currentX)
        val dy = abs(motionY - currentY)

        if ((dx >= touchTolerance || dy >= touchTolerance) && movingCorner != null) {
            if(movingCorner == Corner.TOP) {
                topCornerX = motionX
                topCornerY = motionY
            } else {
                bottomCornerX = motionX
                bottomCornerY = motionY
            }
            view.invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if(active) {
            canvas?.save()
            canvas?.rotate(-rotationDegrees, canvas?.width / 2.0f, canvas?.height / 2.0f)
            canvas?.drawRect(topCornerX, topCornerY, bottomCornerX, bottomCornerY, paintRect)
            canvas?.drawCircle(topCornerX, topCornerY, 15.0f, paintCircle);
            canvas?.drawCircle(bottomCornerX, bottomCornerY, 15.0f, paintCircle);
            canvas?.restore()
        }
    }

    // activate crop tool
    override fun activate() {
        topCornerX = 10f
        topCornerY = 10f
        bottomCornerX = 200f
        bottomCornerY = 200f
        view.setOnTouchListener { view, motionEvent ->
            if (active) {
                motionX = motionEvent.x
                motionY = motionEvent.y
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> touchDown()
                    MotionEvent.ACTION_MOVE -> touchMove()
                }
                view.performClick()
            }
            true
        }
        active = true
        view.invalidate()
    }

    // apply the crop when deactivating
    override fun deactivate() {
        view.setOnTouchListener(null)
        active = false
        view.invalidate()
    }

    fun cropAndSave() {
        deactivate()
        cropCallback?.invoke(topCornerX, topCornerY, bottomCornerX, bottomCornerY)
    }
}
