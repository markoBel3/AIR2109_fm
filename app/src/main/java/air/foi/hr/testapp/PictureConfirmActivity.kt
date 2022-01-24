package air.foi.hr.testapp

import air.foi.hr.piceditframework.MainActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PictureConfirmActivity : AppCompatActivity(R.layout.activity_picture_confirm) {
    lateinit var imageURI: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageURIString = intent.getStringExtra("com.testapp.imageURI")
        imageURI = Uri.parse(imageURIString)
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

        val imgView: ImageView = findViewById(R.id.imagePreview)
        imgView.setImageBitmap(imageBitmap)

        val cancelBtn: ImageButton = findViewById(R.id.cancelButton)
        cancelBtn.setOnClickListener {
            finish()
        }

        val confirmBtn: ImageButton = findViewById(R.id.confirmButton)
        confirmBtn.setOnClickListener {
            startPicEditFramework()
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


    private fun startPicEditFramework() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("com.testapp.imageURI", imageURI.toString())
        startActivity(intent)
    }


}
