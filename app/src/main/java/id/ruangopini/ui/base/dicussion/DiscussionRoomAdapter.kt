package id.ruangopini.ui.base.dicussion

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import id.ruangopini.R
import id.ruangopini.data.model.Discussion
import id.ruangopini.databinding.ItemDiscussionRoomBinding
import id.ruangopini.utils.Helpers.getColorFromAttr
import id.ruangopini.utils.Helpers.hideView

class DiscussionRoomAdapter(
    private val context: Context,
    private val listDiscussion: List<Discussion>
) : RecyclerView.Adapter<DiscussionRoomAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemDiscussionRoomBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDiscussionRoomBinding.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_discussion_room, parent, false)
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discussion = listDiscussion[position]
        with(binding) {
            tvDiscussionRoomName.text = discussion.name
            tvDicussionDesc.text = discussion.desc
            discussion.issueName.let {
                if (it != null) tvPolicyName.text = it
                else contentPolicy.hideView()
            }
            tvAmountOfPerson.text = discussion.people.toString()
            tvAmountOfPost.text = discussion.post.toString()
            discussion.category?.forEach {
                chipGroup.addView(Chip(context).apply {
                    text = it
                    chipBackgroundColor =
                        ColorStateList.valueOf(context.getColorFromAttr(android.R.attr.windowBackground))
                    chipStrokeWidth = 2f
                    chipStrokeColor = ContextCompat.getColorStateList(context, R.color.primary)
                })
            }
        }
    }

    override fun getItemCount(): Int = listDiscussion.size
}