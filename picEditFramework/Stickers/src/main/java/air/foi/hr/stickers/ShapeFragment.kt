package air.foi.hr.stickers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import air.foi.hr.stickers.R

class ShapeFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.3f)

        return inflater.inflate(R.layout.stickers_dialog, container, true)
    }

    private var mShapeListener: ShapeListener? = null
    fun setShapeListener(stickerListener: ShapeListener?) {
        mShapeListener = stickerListener
    }

    interface ShapeListener {
        fun onShapeClick(bitmap: Bitmap?)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contentView = View.inflate(context, R.layout.stickers_dialog, null)
        dialog?.setContentView(contentView)
        val rvEmoji: RecyclerView = contentView.findViewById(R.id.rvEmoji)
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rvEmoji.layoutManager = gridLayoutManager
        val stickerAdapter = StickerAdapter()
        rvEmoji.adapter = stickerAdapter
    }

    inner class StickerAdapter : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {
        var shapeList = intArrayOf(
            R.drawable.shapes_arrow_blue,
            R.drawable.shapes_circle_blue,
            R.drawable.shapes_elipse_blue,
            R.drawable.shapes_rectangle_blue,
            R.drawable.shapes_square_blue,
            R.drawable.shapes_star_blue,
            R.drawable.shapes_triangle_blue,
            R.drawable.shapes_arrow_red,
            R.drawable.shapes_circle_red,
            R.drawable.shapes_elipse_red,
            R.drawable.shapes_rectangle_red,
            R.drawable.shapes_square_red,
            R.drawable.shapes_star_red,
            R.drawable.shapes_triangle_red,
            R.drawable.shapes_arrow_white,
            R.drawable.shapes_circle_white,
            R.drawable.shapes_elipse_white,
            R.drawable.shapes_rectangle_white,
            R.drawable.shapes_square_white,
            R.drawable.shapes_star_white,
            R.drawable.shapes_triangle_white
        )
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_shape, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imgShape.setImageResource(shapeList[position])
        }

        override fun getItemCount(): Int {
            return shapeList.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var imgShape: ImageView = itemView.findViewById(R.id.imgShape)

            init {
                itemView.setOnClickListener {
                    if (mShapeListener != null) {
                        mShapeListener!!.onShapeClick(
                            BitmapFactory.decodeResource(
                                resources,
                                shapeList[layoutPosition]
                            )
                        )
                    }
                    dismiss()
                }
            }
        }
    }
}