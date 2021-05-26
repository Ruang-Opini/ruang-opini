package id.ruangopini.ui.base.dicussion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ruangopini.R
import id.ruangopini.data.model.Discussion
import id.ruangopini.databinding.ItemDiscussionRoomBinding

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
            tvPolicyName.text = discussion.issueName
            tvAmountOfPerson.text = discussion.people.toString()
            tvAmountOfPost.text = discussion.post.toString()
        }
    }

    override fun getItemCount(): Int = listDiscussion.size
}