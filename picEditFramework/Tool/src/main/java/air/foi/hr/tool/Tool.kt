package air.foi.hr.tool

import android.graphics.Canvas
import kotlin.math.cos
import kotlin.math.sin

/**
 * Each tool should extend this base class.
 */
open class Tool(private val undoRedo: UndoRedo) {

    var active : Boolean = false
    var rotationDegrees = 0.0f

    open fun activate() {
        active = true
    }

    open fun deactivate() {
        active = false
    }

    open fun onDraw(canv: Canvas?) {

    }

    open fun clear() {

    }

    open fun pushState() {

    }

    open fun undo() {

    }

    open fun redo() {

    }
}

class Functions {
    companion object {
        fun translateXY(x: Float, y: Float, centerX: Float, centerY: Float, angle: Float): Array<Float> {
            val angleRad = Math.toRadians(angle.toDouble()).toFloat()

            val sin = sin(angleRad)
            val cos = cos(angleRad)

            var x1 = x - centerX
            var y1 = y - centerY

            val xNew = x1 * cos - y1 * sin
            val yNew = x1 * sin + y1 * cos

            x1 = xNew + centerX
            y1 = yNew + centerY

            return arrayOf(x1, y1)
        }
    }

}