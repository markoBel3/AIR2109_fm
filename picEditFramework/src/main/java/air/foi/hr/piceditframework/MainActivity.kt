package air.foi.hr.piceditframework

import air.foi.hr.crop.CropOptions
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import air.foi.hr.piceditview.PicEditView
import air.foi.hr.rotateflip.RotateOptions
import androidx.fragment.app.FragmentTransaction
import air.foi.hr.exportmodule.ExportDialog
import air.foi.hr.filter.Filter
import air.foi.hr.text.TextOptions
import air.foi.hr.stickers.StickersOptions
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the default action bar on top which says "PicEdit framework"
        supportActionBar?.hide()
        setContentView(R.layout.main_with_options)

        val imageURIString = intent.getStringExtra("com.testapp.imageURI")
        val imageURI = Uri.parse(imageURIString)
        var imageBitmap: Bitmap
        contentResolver.openInputStream(imageURI).use { stream ->
            imageBitmap = BitmapFactory.decodeStream(stream)
        }
        contentResolver.openInputStream(imageURI).use { stream ->
            val exif = stream?.let { ExifInterface(it) }
            val rot = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val rotDegrees = rot?.let { exifToDegrees(it) }
            val matrix = Matrix()
            if(rotDegrees != 0) {
                matrix.preRotate(rotDegrees!!.toFloat())
            }
            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.width, imageBitmap.height, matrix, true)
        }
        setupListeners(imageBitmap)
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0)
            }
        }
    }

    private fun setupListeners(bitmap_img: Bitmap) {

        val picEditView = findViewById<PicEditView>(R.id.pic_edit_view)
        picEditView.bitmapImg = bitmap_img
        picEditView.invalidate()


        // Open exporting dialog when clicking on button in menu
        val exportImage = findViewById<ImageView>(R.id.menu_export_icon)
        exportImage.setOnClickListener {
            val dialog = ExportDialog.newInstance(picEditView)
            dialog.show(supportFragmentManager, "exportDialog")
        }

        val penIcon = findViewById<ImageView>(R.id.pen_tool)
        penIcon.setOnClickListener {
            picEditView.deactivateAllTools()
            picEditView.penTool?.activate()
            picEditView.penTool?.isEraser = false
        }

        val brushTool = findViewById<ImageView>(R.id.brush_tool)
        brushTool.setOnClickListener {
            picEditView.penTool?.isEraser = true
        }

        val rotateTool = findViewById<ImageView>(R.id.rotate_tool)
        rotateTool.setOnClickListener{
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val rotateOptionsFragment = RotateOptions.newInstance(picEditView)
            ft.replace(R.id.frame_layout,rotateOptionsFragment)
            ft.commit()
        }

        val stickersTool = findViewById<ImageView>(R.id.sticker_tool)
        stickersTool.setOnClickListener{
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val stickersFragment = StickersOptions.newInstance(picEditView)
            ft.replace(R.id.frame_layout,stickersFragment)
            ft.commit()
        }

        val cutTool = findViewById<ImageView>(R.id.cut_tool)
        cutTool.setOnClickListener{
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val cropFragment = CropOptions.newInstance(picEditView.cropTool)
            ft.replace(R.id.frame_layout,cropFragment)
            ft.commit()

            picEditView.deactivateAllTools()
            picEditView.cropTool?.activate()
        }

        val filterTool = findViewById<ImageView>(R.id.magic_wand_tool)
        filterTool.setOnClickListener{
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val filtersFragment = Filter.newInstance(picEditView)
            ft.replace(R.id.frame_layout,filtersFragment)
            ft.commit()
        }

        val textTool = findViewById<ImageView>(R.id.text_tool)
        textTool.setOnClickListener{
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val textFragment = TextOptions.newInstance(picEditView)
            ft.replace(R.id.frame_layout,textFragment)
            ft.commit()
        }

        val exitFramework = findViewById<ImageView>(R.id.exit_framework)
        exitFramework.setOnClickListener {
            finish()
        }

        val redo = findViewById<ImageView>(R.id.redo)
        redo.setOnClickListener{
            picEditView.undoRedo.redo()
        }
        val undo = findViewById<ImageView>(R.id.undo)
        undo.setOnClickListener{
            picEditView.undoRedo.undo()
        }
    }

    private fun exifToDegrees(orientation: Int): Int {
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270
        }
        return 0
    }
}
