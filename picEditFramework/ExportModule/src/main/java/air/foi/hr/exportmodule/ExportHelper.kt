package air.foi.hr.exportmodule
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.IOException
import android.os.Build
import android.util.Log
import java.io.FileOutputStream
import java.io.OutputStream


//this is now preferred to make a static function without use of a class

private const val SAVEDIR: String = "PicEdit-Images"
@Throws(IOException::class)
/**
 * Saves bitmap as desired image format
 * You can use these [mimeType]: "image/png", "image/jpeg"...
 */
fun saveBitmap(
    context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
    mimeType: String, displayName: String
): Uri {
    val relativeLocation = Environment.DIRECTORY_PICTURES + File.pathSeparator + SAVEDIR
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation)
        }
    }

    val resolver = context.contentResolver
    var uri: Uri? = null

    try {
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Failed to create new MediaStore record.")

        resolver.openOutputStream(uri)?.use {
            if (!bitmap.compress(format, 95, it))
                throw IOException("Failed to save bitmap.")
        } ?: throw IOException("Failed to open output stream.")

        return uri

    } catch (e: IOException) {

        uri?.let { orphanUri ->
            resolver.delete(orphanUri, null, null)
        }

        throw e
    }
}

