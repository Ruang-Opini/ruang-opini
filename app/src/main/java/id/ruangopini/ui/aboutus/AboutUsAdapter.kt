package id.ruangopini.ui.aboutus

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.data.model.AboutUs
import id.ruangopini.databinding.ItemAboutBinding

class AboutUsAdapter(
    private val listAboutUs: List<AboutUs>
) : RecyclerView.Adapter<AboutUsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemAboutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAboutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAboutUs[position]
        binding.apply {
            tvTitleAbout.text = data.title
            val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(data.content, Html.FROM_HTML_MODE_COMPACT)
            else Html.fromHtml(data.content)
            tvContentAbout.text = content
        }
    }

    override fun getItemCount(): Int = listAboutUs.size
}