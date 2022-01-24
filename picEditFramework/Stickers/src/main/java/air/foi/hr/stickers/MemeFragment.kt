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

class MemeFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.3f)

        return inflater.inflate(R.layout.stickers_dialog, container, true)
    }

    private var mStickerListener: StickerListener? = null
    fun setStickerListener(stickerListener: StickerListener?) {
        mStickerListener = stickerListener
    }

    interface StickerListener {
        fun onStickerClick(bitmap: Bitmap?)
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
        var stickerList = intArrayOf(
            R.drawable.meme_bad_luck_brian,
            R.drawable.meme_doge,
            R.drawable.meme_crow,
            R.drawable.meme_derp_face,
            R.drawable.meme_iq,
            R.drawable.meme_derp_lol,
            R.drawable.meme_donald_duck,
            R.drawable.meme_drake_no,
            R.drawable.meme_drake_yes,
            R.drawable.meme_feels_bad_man,
            R.drawable.meme_grumpy_cat,
            R.drawable.meme_guy_interview,
            R.drawable.meme_mr_bean,
            R.drawable.meme_nametag,
            R.drawable.meme_nyan_cat,
            R.drawable.meme_panik,
            R.drawable.meme_salt_bae,
            R.drawable.meme_sponge_bob,
            R.drawable.meme_success_kid,
            R.drawable.meme_tom_train,
            R.drawable.meme_we_want_you
        )
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.row_sticker, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imgSticker.setImageResource(stickerList[position])
        }

        override fun getItemCount(): Int {
            return stickerList.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var imgSticker: ImageView = itemView.findViewById(R.id.imgSticker)

            init {
                itemView.setOnClickListener {
                    if (mStickerListener != null) {
                        mStickerListener!!.onStickerClick(
                            BitmapFactory.decodeResource(
                                resources,
                                stickerList[layoutPosition]
                            )
                        )
                    }
                    dismiss()
                }
            }
        }
    }
}