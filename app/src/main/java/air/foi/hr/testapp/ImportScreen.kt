package air.foi.hr.testapp
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger



class ImportScreen : AppCompatActivity(R.layout.activity_import_screen) {
    lateinit var currentPhotoURI: Uri
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, PictureConfirmActivity::class.java)
            intent.putExtra("com.testapp.imageURI", currentPhotoURI.toString())
            startActivity(intent)
        }
    }

    private val fileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val res = result.data
            currentPhotoURI = res?.data!!

            val intent = Intent(this, PictureConfirmActivity::class.java)
            intent.putExtra("com.testapp.imageURI", currentPhotoURI.toString())
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraBtn: Button = findViewById(R.id.Camera_button)
        val fileBtn: Button = findViewById(R.id.Gallery_button)
        cameraBtn.setOnClickListener {
            takePicture();
        }
        fileBtn.setOnClickListener {
            openFilesystem();
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Checks if a camera app exists (will not work on simulator)
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Logger.getLogger(MainActivity::class.java.name).warning("Exception: " + ex.message)
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.testapp.fileprovider",
                        it
                    )
                    currentPhotoURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun openFilesystem() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        fileLauncher.launch(intent)
    }
}
