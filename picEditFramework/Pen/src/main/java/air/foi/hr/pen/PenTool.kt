package air.foi.hr.pen

import android.view.MotionEvent
import android.view.View
import air.foi.hr.tool.Tool
import air.foi.hr.tool.Functions
import air.foi.hr.tool.UndoRedo
import android.graphics.*

data class PathItem (val path: Path, val paint: Paint)

class PenTool(private val view: View, private val w: Int, private val h: Int, private val undoRedo: UndoRedo) : Tool(undoRedo = undoRedo) {
    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth=45f
        style = Paint.Style.STROKE
    }
    private val eraserPaint = Paint().apply {
        color = Color.GREEN
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.STROKE
        maskFilter = null
        strokeWidth=45f
        isAntiAlias = true
    }

    var isEraser = false

    private var path = Path()
    private var undoPaths: ArrayList<PathItem> = ArrayList()
    private var redoPaths: ArrayList<PathItem> = ArrayList()

    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private fun pushState(path: PathItem) {
        undoRedo.pushState(this)
        undoPaths.add(path)
        redoPaths.clear()
    }

    override fun undo() {
        if (undoPaths.isNotEmpty()) {
            redoPaths.add(undoPaths.removeAt(undoPaths.lastIndex))
            view.invalidate()
        }
    }

    override fun redo() {
        if (redoPaths.isNotEmpty()) {
            undoPaths.add(redoPaths.removeAt(redoPaths.lastIndex))
            view.invalidate()
        }
    }


    override fun activate() {
        super.activate()
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
    }

    override fun deactivate() {
        super.deactivate()
        view.setOnTouchListener(null)
    }

    private fun touchDown() {
        path = Path()
        pushState(PathItem(path, if (isEraser) eraserPaint else paint))

        val centerX = w / 2.0f
        val centerY = h / 2.0f
        val translated = Functions.translateXY(motionX, motionY, centerX, centerY, -rotationDegrees)
        path.moveTo(translated[0], translated[1])
        currentX = translated[0]
        currentY = translated[1]
    }

    private fun touchMove() {
        val centerX = w / 2.0f
        val centerY = h / 2.0f
        val translated = Functions.translateXY(motionX, motionY, centerX, centerY, -rotationDegrees)
        path.lineTo(translated[0], translated[1])
        view.invalidate()
    }

    override fun onDraw(canv: Canvas?) {
        undoPaths.forEach {
            canv?.drawPath(it.path, it.paint)
        }
    }

    override fun clear() {
        undoPaths.clear()
    }
}
