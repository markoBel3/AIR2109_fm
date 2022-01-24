package air.foi.hr.stickers

import air.foi.hr.piceditview.PicEditView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import air.foi.hr.stickers.R

class StickersOptions : Fragment() {
    lateinit var currentPhotoURI: Uri
    private val fileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val res = result.data
            currentPhotoURI = res?.data!!

            /*val intent = Intent(this, PictureConfirmActivity::class.java)
            intent.putExtra("com.testapp.imageURI", currentPhotoURI.toString())
            startActivity(intent)*/
        }
    }
    private var picEditView: PicEditView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_stickers, container, false)
        val exitIcon = view.findViewById<ImageView>(R.id.exit_image)
        exitIcon.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }

        val fileIcon: ImageView = view.findViewById(R.id.sticker_add)
        fileIcon.setOnClickListener {
            openFilesystem();
        }

        val emojiIcon = view.findViewById<ImageView>(R.id.sticker_emoji)
        emojiIcon.setOnClickListener {
            val dialog = EmojiFragment()
            dialog.show(activity?.supportFragmentManager!!, "emojiDialog")
        }

        val memeIcon = view.findViewById<ImageView>(R.id.sticker_meme)
        memeIcon.setOnClickListener {
            val dialog = MemeFragment()
            dialog.show(activity?.supportFragmentManager!!, "memeDialog")
        }

        val shapeIcon = view.findViewById<ImageView>(R.id.sticker_shapes)
        shapeIcon.setOnClickListener {
            val dialog = ShapeFragment()
            dialog.show(activity?.supportFragmentManager!!, "shapeDialog")
        }

        return view
    }

    private fun openFilesystem() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        fileLauncher.launch(intent)
    }

    companion object {
        fun newInstance(picEditView: PicEditView): StickersOptions {
            val fragment = StickersOptions()
            fragment.picEditView = picEditView
            return fragment
        }
    }
}