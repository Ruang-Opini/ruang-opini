package id.ruangopini.ui.post.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.OriginalSize
import coil.transform.RoundedCornersTransformation
import com.stfalcon.imageviewer.StfalconImageViewer
import id.ruangopini.databinding.ItemImagePostBinding
import id.ruangopini.utils.Helpers.showView


class ImagePostAdapter(
    private val context: Context,
    private val listOfImage: List<String>,
    private val closeListener: CloseListener? = null,
) : RecyclerView.Adapter<ImagePostAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemImagePostBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemImagePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.ivPost.apply {
            if (listOfImage.size == 1) {
                this.layoutParams.apply {
                    height = ConstraintLayout.LayoutParams.MATCH_PARENT
                    width = ConstraintLayout.LayoutParams.MATCH_PARENT
                }
                binding.contentImagePost.apply {
                    layoutParams.apply {
                        height = ConstraintLayout.LayoutParams.MATCH_PARENT
                        width = ConstraintLayout.LayoutParams.MATCH_PARENT
                    }
                    requestLayout()
                }
                this.requestLayout()
            }
            load(listOfImage[position]) {
                crossfade(true)
                transformations(RoundedCornersTransformation(8f))
                size(OriginalSize)
            }
            setOnClickListener {
                StfalconImageViewer.Builder(context, listOfImage) { imageView, image ->
                    imageView.load(image) { crossfade(true) }
                }.withStartPosition(position).show()
            }

        }
        closeListener?.let { listener ->
            binding.btnClose.apply {
                showView()
                setOnClickListener { listener.onClose(position) }
            }
        }
    }


    override fun getItemCount(): Int = listOfImage.size
}

interface CloseListener {
    fun onClose(position: Int)
}