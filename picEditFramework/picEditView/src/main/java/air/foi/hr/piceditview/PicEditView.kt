package air.foi.hr.piceditview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import air.foi.hr.tool.Tool
import air.foi.hr.crop.CropTool
import air.foi.hr.pen.PenTool
import air.foi.hr.tool.UndoRedo
import kotlin.math.absoluteValue
import android.os.Handler
import android.os.Looper

/**
 * View holding the bitmap and canvas.
 */
class PicEditView
@JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attributes, defStyleAttr) {
    lateinit var bitmapImg: Bitmap

    private lateinit var backgroundBitmap: Bitmap
    private lateinit var backgroundCanvas: Canvas

    private lateinit var penBitmap: Bitmap
    private lateinit var penCanvas: Canvas

    private lateinit var masterCanvas: Canvas
    lateinit var masterBitmap: Bitmap

    val undoRedo = UndoRedo()

    var cropTool: CropTool? = null
    var penTool: PenTool? = null

    var allTools: MutableList<Tool?> = mutableListOf()

    var rotationDegrees = 0.0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        backgroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        backgroundCanvas = Canvas(backgroundBitmap)

        masterBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        masterCanvas = Canvas(masterBitmap)

        initializeCropTool()
        initializePenTool(w, h)


    }

    fun clearAllTools() {
        for(tool in allTools) {
            tool?.rotationDegrees = 0.0f
            rotationDegrees = 0.0f
            tool?.clear()
        }
    }


    fun deactivateAllTools() {
        for(tool in allTools) {
            tool?.deactivate()
        }
    }


    // Called each time invalidate() is called (and when view is initially drawn).
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        backgroundBitmap.eraseColor(Color.TRANSPARENT)
        penBitmap.eraseColor(Color.TRANSPARENT)
        masterBitmap.eraseColor(Color.TRANSPARENT)

        penCanvas.save()
        backgroundCanvas.save()

        penCanvas.rotate(rotationDegrees, width / 2.0f, height / 2.0f)
        backgroundCanvas.rotate(rotationDegrees, width / 2.0f, height / 2.0f)

        backgroundCanvas.drawBitmap(bitmapImg, 0f, 0f, null)

        for(tool in allTools) {
            if (tool is PenTool) {
                tool.onDraw(penCanvas)
            } else {
                tool?.onDraw(backgroundCanvas)
            }
        }

        penCanvas.restore()
        backgroundCanvas.restore()

        masterCanvas.drawBitmap(backgroundBitmap, 0f, 0f, null)
        masterCanvas.drawBitmap(penBitmap, 0f, 0f, null)

        canvas?.drawBitmap(masterBitmap, 0f, 0f, null)


    }

    private fun initializeCropTool() {
        cropTool = CropTool(this, undoRedo)
        allTools.add(cropTool)
        cropTool?.cropCallback = { topCornerX, topCornerY, bottomCornerX, bottomCornerY ->
            // this is a bit of a hack
            // invalidate() does not wait for the canvas to be redrawn before continuing
            // which is why we delay execution by 100ms so we give crop tool some time
            // to disappear, otherwise the cropped image has the crop tool still in it
            // there are better ways to do this but they require passing  custom callbacks
            // into picEditView's onDraw, which is way too messy IMO, this is fine
            // - Gazdek
            Handler(Looper.getMainLooper()).postDelayed({
                val x = topCornerX.toInt()
                val y = topCornerY.toInt()
                val width = (topCornerX - bottomCornerX).toInt().absoluteValue
                val height = (topCornerY - bottomCornerY).toInt().absoluteValue
                val rightVertex = x + width
                val bottomVertex = y + height
                if(rightVertex < bitmapImg.width && bottomVertex < bitmapImg.height) {
                    bitmapImg = Bitmap.createBitmap(masterBitmap, x, y, width, height)
                    // BAKE THE BITMAP by clearing all tools and setting bitmapImg to the
                    // bitmap that has been drawn by onDraw
                    clearAllTools()
                    invalidate()
                }
            }, 100)
        }
    }

    private fun initializePenTool(w: Int, h: Int) {
        penBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        penCanvas = Canvas(penBitmap)

        penTool = PenTool(this, w, h, undoRedo)
        allTools.add(penTool)
    }
}
