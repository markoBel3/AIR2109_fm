package air.foi.hr.text

import air.foi.hr.piceditview.PicEditView
import air.foi.hr.text.R.*
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class TextOptions : Fragment() {

    private var picEditView: PicEditView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(layout.fragment_text_options, container, false)
        val cancel = view.findViewById<ImageView>(R.id.text_cancel)
        val fonts = view.findViewById<RelativeLayout>(R.id.fonts)
        val color = view.findViewById<RelativeLayout>(R.id.colors)
        val colorBackground = view.findViewById<RelativeLayout>(R.id.bgcolors)
        setListeners(cancel, fonts, color, colorBackground)
        return view
    }

    private fun setListeners(cancel: ImageView?, fonts: RelativeLayout?, color: RelativeLayout?, colorBackground: RelativeLayout?) {
        cancel?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        fonts?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        color?.setOnClickListener {
            showDialog(requireView())
        }
        colorBackground?.setOnClickListener {
            showDialog(requireView())
        }
    }

    //Opening a color picker dialog using builder method
    fun showDialog(view: View) {
        val builder = ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker")
            .setPreferenceName("PicEditView")
            .setPositiveButton(
                getString(R.string.confirm),
                ColorEnvelopeListener { envelope, _ -> setLayoutColor(envelope, view) }
            )
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.colorPickerView.flagView = BubbleFlag(context).apply { flagMode = FlagMode.FADE }
        builder.show()
    }

    //This method serves a purpose to hand over the envelope which contains chosen color attributes
    //An example of simple textview for hex value and this linearlayout for color
    private fun setLayoutColor(envelope: ColorEnvelope, view: View) {
        val textView = view.findViewById<TextView>(R.id.colors_text)
        textView.text = "#${envelope.hexCode}"
        /*
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout.setBackgroundColor(envelope.color)
         */
    }

    companion object {
        fun newInstance(picEditView: PicEditView): TextOptions {
            val fragment = TextOptions()
            fragment.picEditView = picEditView
            return fragment
        }
    }
}