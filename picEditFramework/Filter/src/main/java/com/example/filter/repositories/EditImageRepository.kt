package air.foi.hr.filter.repositories

import air.foi.hr.filter.data.ImageFilter
import android.graphics.Bitmap

interface EditImageRepository {
    fun getImageFilters(image : Bitmap) : List<ImageFilter>
}