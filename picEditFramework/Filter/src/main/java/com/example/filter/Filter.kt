package air.foi.hr.filter

import air.foi.hr.filter.adapters.EditImageAdapter
import air.foi.hr.filter.data.ImageFilter
import air.foi.hr.filter.listeners.EditImageListener
import air.foi.hr.filter.repositories.EditImageRepository
import air.foi.hr.filter.repositories.EditImageRepositoryImpl
import air.foi.hr.piceditview.PicEditView
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import jp.co.cyberagent.android.gpuimage.GPUImage

class Filter : Fragment(), EditImageListener {

    private var picEditView: PicEditView? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var gpuImage: GPUImage

    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_filter, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.filtersRecyclerView)
        val cancel = view.findViewById<ImageView>(R.id.filter_cancel)
        val accept = view.findViewById<ImageView>(R.id.filter_accept)
        setListeners(accept, cancel)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gpuImage = GPUImage(activity?.applicationContext!!)
        originalBitmap = picEditView?.bitmapImg!!
        filteredBitmap.value = originalBitmap
        with(originalBitmap) {
            gpuImage.setImage(this)
        }
        val repositoryImpl: EditImageRepository = EditImageRepositoryImpl(activity?.applicationContext!!)
        val adapter = EditImageAdapter(repositoryImpl.getImageFilters(originalBitmap), this)
        recyclerView.adapter = adapter
        filteredBitmap.observe(viewLifecycleOwner, { bitmap ->
            picEditView?.let { it -> setImage(it, bitmap) }
        })
    }


    private fun setListeners(accept: ImageView, cancel: ImageView) {
        cancel.setOnClickListener {
            picEditView?.let { it -> setImage(it, originalBitmap) }
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        accept.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    companion object {
        fun newInstance(picEditView: PicEditView): Filter {
            val fragment = Filter()
            fragment.picEditView = picEditView
            return fragment
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}