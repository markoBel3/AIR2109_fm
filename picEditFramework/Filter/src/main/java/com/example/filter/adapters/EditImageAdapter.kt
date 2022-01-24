package air.foi.hr.filter.adapters

import air.foi.hr.filter.R
import air.foi.hr.filter.data.ImageFilter
import air.foi.hr.filter.listeners.EditImageListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView

class EditImageAdapter(
    private var imageFilters: List<ImageFilter>,
    private val imageFilterListener: EditImageListener
    ) : RecyclerView.Adapter<EditImageAdapter.EditImageViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0

    inner class  EditImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: RoundedImageView = itemView.findViewById(R.id.imageFilterPreview)
        val textView: TextView = itemView.findViewById(R.id.textFilterName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container, parent, false)
        return EditImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditImageViewHolder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                imageView.setImageBitmap(filterPreview)
                textView.text = name
                itemView.setOnClickListener {
                    if(position != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = holder.adapterPosition
                        with(this@EditImageAdapter) {
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
            }
            textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    if(selectedFilterPosition == position)
                        R.color.FilterTextColorMarked
                    else
                        R.color.FilterTextColor
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return imageFilters.size
    }
}