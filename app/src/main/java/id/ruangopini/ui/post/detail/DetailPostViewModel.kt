package id.ruangopini.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Comment
import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.analytics.FirestoreAnalyticsRepository
import id.ruangopini.data.repo.remote.firebase.firestore.comment.FirestoreCommentRepository
import id.ruangopini.data.repo.remote.firebase.firestore.post.FirestorePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailPostViewModel(
    private val postRepository: FirestorePostRepository,
    private val commentRepository: FirestoreCommentRepository,
    private val analyticsRepository: FirestoreAnalyticsRepository
) : ViewModel() {

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post

    fun getPostById(postId: String) = viewModelScope.launch {
        postRepository.getPostById(postId).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data -> _post.value = data }
                }
                is State.Failed -> {
                }
            }
        }
    }

    fun updateVoteUp(vote: Int, postId: String) = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (vote == 1) {
            postRepository.addToVote(postId, userId, true).collect()
            postRepository.remoteFromVote(postId, userId, false).collect()
        } else postRepository.remoteFromVote(postId, userId, true).collect()
        postRepository.updateVoteUp(vote, postId).collect()
        analyticsRepository.updateVoteUpPost(postId, vote).collect()
    }

    fun updateVoteDown(vote: Int, postId: String) = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (vote == 1) {
            postRepository.addToVote(postId, userId, false).collect()
            postRepository.remoteFromVote(postId, userId, true).collect()
        } else postRepository.remoteFromVote(postId, userId, false).collect()
        postRepository.updateVoteDown(vote, postId).collect()
        analyticsRepository.updateVoteDownPost(postId, vote).collect()
    }


    private val _comment = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>> get() = _comment

    fun postComment(comment: Comment, discussionId: String) = viewModelScope.launch {
        postRepository.addComment(comment.postId ?: "").collect()
        commentRepository.createComment(comment).collect()
        analyticsRepository.updateCommentDiscussion(discussionId).collect()
        analyticsRepository.updateCommentPost(comment.postId ?: "").collect()
    }

    fun getComments(postId: String) = viewModelScope.launch {
        commentRepository.loadComment(postId).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data -> _comment.value = data }
                }
                is State.Failed -> {
                }
            }
        }
    }

}