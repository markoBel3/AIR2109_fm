package air.foi.hr.rotateflip

import air.foi.hr.piceditview.PicEditView
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.RequiresApi


/**
 * A simple [Fragment] subclass.
 * Use the [RotateOptions.newInstance] factory method to
 * create an instance of this fragment.
 */
class RotateOptions : Fragment() {
    private var picEditView: PicEditView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_rotate_options, container, false)
        val rotateIcon = view.findViewById<ImageView>(R.id.rotate_image)
        rotateIcon.setOnClickListener{
            picEditView?.let { rotate(it,picEditView?.rotationDegrees?.plus(90.0f)!!) }
            if(picEditView?.rotationDegrees!! > 360.0f) {
                picEditView?.let { rotate(it,picEditView?.rotationDegrees?.minus(360.0f)!!) }
            }
        }

        val exitIcon = view.findViewById<ImageView>(R.id.exit_image)
        exitIcon.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();

        }

        val flipIcon = view.findViewById<ImageView>(R.id.flip_image)
        flipIcon.setOnClickListener {
            picEditView?.let { it1 -> flip(it1) }
        }

        val seekBarWidget = view.findViewById<SeekBar>(R.id.seekBar)
        seekBarWidget.max = 89
        seekBarWidget.progress = 45
        seekBarWidget?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                picEditView?.let { rotate(it,seekBarWidget.progress.toFloat() - 45.0f) }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {


            }
        })
        return view
    }

    companion object {
        fun newInstance(picEditView: PicEditView): RotateOptions {
            val fragment = RotateOptions()
            fragment.picEditView = picEditView
            return fragment
        }
    }

}