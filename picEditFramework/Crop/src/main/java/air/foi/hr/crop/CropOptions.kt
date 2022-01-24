package air.foi.hr.crop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class CropOptions : Fragment() {

    private var toolRef: CropTool? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_crop_options, container, false)
        val exitIcon = view.findViewById<ImageView>(R.id.exit_image)
        val cropIcon = view.findViewById<ImageView>(R.id.crop_image)
        exitIcon.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
            toolRef?.deactivate()
        }

        cropIcon.setOnClickListener {
            toolRef?.cropAndSave()
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();


        }



        return view
    }


    companion object {
        fun newInstance(toolRef: CropTool?): CropOptions {
            val fragment = CropOptions()
            fragment.toolRef = toolRef
            return fragment
        }
    }
}