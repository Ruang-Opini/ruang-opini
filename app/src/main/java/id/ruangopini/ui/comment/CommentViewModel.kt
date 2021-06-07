package id.ruangopini.ui.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Comment
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.comment.FirestoreCommentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CommentViewModel(
    private val commentRepository: FirestoreCommentRepository
) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    fun getCommentByUserId() = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        commentRepository.getCommentByUserId(userId).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data -> _comments.value = data }
                }
                is State.Failed -> {
                    Log.d("TAG", "getCommentByUserId: failed = ${it.message}")
                }
            }
        }
    }
}