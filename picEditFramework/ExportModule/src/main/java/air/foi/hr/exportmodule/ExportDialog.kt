package air.foi.hr.exportmodule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import air.foi.hr.piceditview.PicEditView
import android.graphics.Bitmap

class ExportDialog : DialogFragment(){
    var selectedExportType = 0
    var picEditView: PicEditView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.3f)

        return inflater.inflate(R.layout.export_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exportTiff = view.findViewById<TextView>(R.id.export_tiff)
        changeTextViewStates(exportTiff)
        exportTiff.setOnClickListener{
            changeTextViewStates(exportTiff)
        }

        val exportJpg = view.findViewById<TextView>(R.id.export_jpg)
        exportJpg.setOnClickListener{
            changeTextViewStates(exportJpg)
        }

        val exportPng = view.findViewById<TextView>(R.id.export_png)
        exportPng.setOnClickListener{
            changeTextViewStates(exportPng)
        }

        val exportSave = view.findViewById<TextView>(R.id.export_save)
        exportSave.setOnClickListener {
            when(selectedExportType){
                0 -> {
                    this.context?.let { it1 -> picEditView?.bitmapImg?.let { it2 ->
                        saveBitmap(it1,
                            it2, Bitmap.CompressFormat.JPEG, "image/tiff","exportPic")
                    } }
                }
                1 -> {
                    this.context?.let { it1 -> picEditView?.bitmapImg?.let { it2 ->
                        saveBitmap(it1,
                            it2, Bitmap.CompressFormat.JPEG, "image/jpg","exportPic")
                    } }
                }
                2 -> {
                    this.context?.let { it1 -> picEditView?.bitmapImg?.let { it2 ->
                        saveBitmap(it1,
                            it2, Bitmap.CompressFormat.PNG, "image/png","exportPic")
                    } }
                }
            }
        }
    }

    private fun changeTextViewStates(textView: TextView?) {
        val textViewId = textView?.id
        val exportTiff = view?.findViewById<TextView>(R.id.export_tiff)

        val exportJpg = view?.findViewById<TextView>(R.id.export_jpg)

        val exportPng = view?.findViewById<TextView>(R.id.export_png)

        when (textViewId) {
            R.id.export_tiff -> {
                exportTiff?.setBackgroundResource(R.drawable.export_selected_format_rounded_corner)
                exportJpg?.setBackgroundResource(0)
                exportPng?.setBackgroundResource(0)
                selectedExportType = 0
            }
            R.id.export_jpg -> {
                exportTiff?.setBackgroundResource(0)
                exportJpg?.setBackgroundResource(R.drawable.export_selected_format_rounded_corner)
                exportPng?.setBackgroundResource(0)
                selectedExportType = 1
            }
            R.id.export_png -> {
                exportTiff?.setBackgroundResource(0)
                exportJpg?.setBackgroundResource(0)
                exportPng?.setBackgroundResource(R.drawable.export_selected_format_rounded_corner)
                selectedExportType = 2
            }
        }
    }

    companion object {
        fun newInstance(picEditView: PicEditView): ExportDialog {
            val fragment = ExportDialog()
            fragment.picEditView = picEditView
            return fragment
        }
    }
}