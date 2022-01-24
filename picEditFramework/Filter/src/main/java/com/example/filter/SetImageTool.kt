package air.foi.hr.filter

import air.foi.hr.piceditview.PicEditView
import android.graphics.Bitmap
import android.graphics.Matrix

fun setImage(view: PicEditView, bitmap: Bitmap) {
    val matrix = Matrix()
    view.bitmapImg = Bitmap.createBitmap(bitmap,0, 0, view.bitmapImg.width, view.bitmapImg.height, matrix,true)
    view.invalidate()
}