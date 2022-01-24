package air.foi.hr.filter.listeners

import air.foi.hr.filter.data.ImageFilter


interface EditImageListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}