package id.ruangopini.ui.post.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.post.FirestorePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PostViewModel(
    private val postRepository: FirestorePostRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _post = MutableLiveData<List<Post>>()
    val post: LiveData<List<Post>> get() = _post

    fun getPostByUserId(userId: String) = viewModelScope.launch {
        postRepository.getPostByUserId(userId).collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> {
                    _isLoading.value = false
                    it.data.let { data -> _post.value = data }
                }
                is State.Failed -> _isLoading.value = false
            }
        }
    }

    fun getPostByDiscussionLatest(discussionId: String) = viewModelScope.launch {
        postRepository.getPostByDiscussionId(discussionId).collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> {
                    _isLoading.value = false
                    it.data.let { data -> _post.value = data }
                }
                is State.Failed -> _isLoading.value = false
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
    }

    fun updateVoteDown(vote: Int, postId: String) = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        if (vote == 1) {
            postRepository.addToVote(postId, userId, false).collect()
            postRepository.remoteFromVote(postId, userId, true).collect()
        } else postRepository.remoteFromVote(postId, userId, false).collect()
        postRepository.updateVoteDown(vote, postId).collect()
    }
}