package air.foi.hr.rotateflip

import air.foi.hr.piceditview.PicEditView
import android.graphics.*
import android.graphics.Bitmap

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun rotate(view: PicEditView, degrees: Float) {
    view.rotationDegrees = degrees
    for(tool in view.allTools) {
        tool?.rotationDegrees = degrees
    }
    view.invalidate()
}



fun flip(view: PicEditView){
    val matrix = Matrix()
    matrix.postScale(-1f, 1f, view.masterBitmap.width / 2f, view.masterBitmap.height / 2f)
    view.bitmapImg = Bitmap.createBitmap(view.masterBitmap, 0, 0, view.masterBitmap.width, view.masterBitmap.height, matrix, true)
    view.clearAllTools()
    view.invalidate()
}
